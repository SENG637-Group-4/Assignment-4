## Section 2 – Analysis of 10 Mutants of the Range Class

The following analysis is based on running **Pitest** with **All Mutators** enabled on the `Range` test suite from Assignment 3. Each mutant below represents a distinct mutation operator and outcome. All line numbers refer to `Range.java` in the JFreeChart Lab4 project.

---

### Mutant 1 — Constructor: Conditional Boundary Changed (Line 90)

**Original:** `if (lower > upper)`  
**Mutated:** `if (lower >= upper)` — changed `>` to `>=`  
**Status:** SURVIVED

**Analysis:** This mutant changes the constructor's guard so that creating `new Range(5.0, 5.0)` (equal bounds) would also throw `IllegalArgumentException`. It survived because the test suite never constructs an equal-bound range and then *asserts that no exception is thrown*. A test `assertDoesNotThrow(() -> new Range(3.0, 3.0))` would kill it. This is a precision gap: the original suite tests that invalid ranges throw, but never explicitly confirms that the equal-bound edge case is valid.

---

### Mutant 2 — Constructor: Post-increment on Parameter (Line 95)

**Original:** `this.lower = lower;`  
**Mutated:** `this.lower = lower++;` — post-increment applied before assignment  
**Status:** SURVIVED

**Analysis:** This is an **equivalent mutant**. In Java, `lower++` returns the original value of `lower` and then increments the local variable — but since `lower` is a parameter that is never used again after this line, the field receives the correct value. No test can observe any difference in behaviour. This demonstrates the classic equivalent mutant pattern: arithmetic mutation on a value that is used exactly once before being discarded produces no observable side effect. Automatic detection tools cannot distinguish this from a non-equivalent mutant without semantic analysis.

---

### Mutant 3 — `getLowerBound()`: Return Replaced with 0.0 (Line 105)

**Original:** `return this.lower;`  
**Mutated:** `return 0.0d;`  
**Status:** KILLED

**Analysis:** This mutant is killed by `getLowerBoundWithPositiveLower()` (expects `2.0` for `Range(2.0, 6.0)`) and `getLowerBoundWithNegativeLower()` (expects `-5.0`). Because the test suite asserts specific non-zero return values for getLowerBound, the 0.0 substitution is immediately caught. This confirms the getter tests are correctly written.

---

### Mutant 4 — `getLength()`: Subtraction Replaced with Addition (Line 123)

**Original:** `return this.upper - this.lower;`  
**Mutated:** `return this.upper + this.lower;`  
**Status:** SURVIVED (NO_COVERAGE)

**Analysis:** `getLength()` was not reached by any test in the A3 suite during this particular Pitest run — it shows as `NO_COVERAGE`. This is a critical gap: the mutant produces `upper + lower` instead of `upper - lower`, which for `Range(2.0, 8.0)` gives `10.0` instead of `6.0` — an obvious, easy-to-catch error. The `RangeGetLengthTest.java` from Assignment 3 covers this method but must be included in the same run to contribute. Lesson: all test files must be inside the same Pitest scope.

---

### Mutant 5 — `intersects(double, double)`: Conditional Boundary Changed (Line 157)

**Original:** `if (b0 <= this.lower)`  
**Mutated:** `if (b0 < this.lower)` — changed `<=` to `<`  
**Status:** SURVIVED

**Analysis:** When `b0` equals `this.lower` exactly, the original takes the `<=` branch and returns `b1 > this.lower`; the mutant falls through to the `else` branch instead. The test suite had no case where `b0 == lower` exactly. A test `intersects(-10.0, 5.0)` on `Range(-10, 10)` — where b0 is exactly at the lower boundary — would expose the different return value and kill this mutant.

---

### Mutant 6 — `constrain()`: Negated Conditional (Line 189)

**Original:** `if (!contains(value))`  
**Mutated:** `if (contains(value))` — negated  
**Status:** SURVIVED (NO_COVERAGE)

**Analysis:** This severe mutant completely inverts the constrain logic: it would try to "constrain" values that are *inside* the range and return values *outside* unchanged. It shows as NO_COVERAGE, meaning `constrain()` was not called at all by the suite in this run. Our `RangeConstrainTest.java` (which tests all three branch outcomes including NaN) addresses this gap. Once included in the Pitest scope this mutant would be killed by any test that calls `constrain()` with a value outside the range.

---

### Mutant 7 — `combine()`: Negated Null Check for range1 (Line 217)

**Original:** `if (range1 == null)`  
**Mutated:** negated conditional — effectively `if (range1 != null)`  
**Status:** SURVIVED

**Analysis:** With the negation, the method returns `range2` whenever `range1` is *non-null* (i.e., always for the normal case) and only enters the range1-null path when range1 *is* null — a complete inversion of null handling. This survived because `Range.combine()` was not tested at all in the A3 suite. Our `RangeCombineTest.java` kills this directly with `combineWithNullRange1ReturnsRange2()` which verifies that when range1 is null the returned object is exactly `range2`, not some other value.

---

### Mutant 8 — `expandToInclude()`: Conditional Boundary Changed (Line 305)

**Original:** `if (value < range.getLowerBound())`  
**Mutated:** `if (value <= range.getLowerBound())` — `<` to `<=`  
**Status:** SURVIVED

**Analysis:** When `value` exactly equals the lower bound, the spec says to return the original range unchanged (value is already included). The mutant instead creates a new `Range(value, upper)` — which has the same numeric bounds but is a *different object*. The boundary case `expandToInclude(Range(-5,5), -5.0)` is not tested. A reference-equality check or any test with `value == lowerBound` that asserts the returned range equals the original would kill this mutant.

---

### Mutant 9 — `expand()`: Multiplication Replaced with Division (Line 331)

**Original:** `double lower = range.getLowerBound() - length * lowerMargin;`  
**Mutated:** `double lower = range.getLowerBound() - length / lowerMargin;`  
**Status:** SURVIVED

**Analysis:** For `Range(0, 10)` with `lowerMargin=0.5`, the correct lower is `0 - 10*0.5 = -5.0`; the mutant gives `0 - 10/0.5 = -20.0`. This is a large numerical difference. The mutant survived because `expand()` was not tested at all. Any test that asserts the exact numeric output of `expand()` would immediately kill it. Our `RangeExpandAdditionalTest.java` covers this with `expandByTenPercentOnEachSide()` and `expandUpperOnlyWithZeroLowerMargin()`.

---

### Mutant 10 — `scale()`: Conditional Boundary Changed (Line 410)

**Original:** `if (factor < 0)`  
**Mutated:** `if (factor <= 0)` — `<` to `<=`  
**Status:** SURVIVED

**Analysis:** This mutant causes `scale(range, 0.0)` to throw `IllegalArgumentException` even though a factor of zero is mathematically valid (scaling a range by zero should produce `Range(0.0, 0.0)`). It survived because `scale()` was never tested. Our `RangeScaleTest.java` kills this with `scaleWithZeroFactorProducesZeroRange()` — which calls `scale(new Range(1.0, 5.0), 0.0)`, asserts no exception is thrown, and checks that both bounds are `0.0`. The `<=` mutant would throw an exception here, making the test fail and the mutant die.

---

### Summary Table of 10 Analysed Mutants

| # | Line | Method | Mutation Operator | Status | Killed By / Why Survived |
|---|------|--------|-------------------|--------|--------------------------|
| 1 | 90 | `Range()` constructor | Conditional boundary `>` → `>=` | SURVIVED | No test constructs equal bounds and asserts no exception |
| 2 | 95 | `Range()` constructor | Post-increment on local param before assign | SURVIVED | **Equivalent mutant** — post-increment on parameter discarded after use |
| 3 | 105 | `getLowerBound()` | Return value replaced with `0.0d` | KILLED | `getLowerBoundWithPositiveLower()`, `getLowerBoundWithNegativeLower()` |
| 4 | 123 | `getLength()` | Subtraction replaced with addition | SURVIVED (NO_COV) | `getLength()` not reached; include `RangeGetLengthTest` in Pitest scope |
| 5 | 157 | `intersects(d,d)` | Conditional boundary `<=` → `<` | SURVIVED | No test with `b0` exactly equal to lower bound |
| 6 | 189 | `constrain()` | Negated conditional | SURVIVED (NO_COV) | `constrain()` not called; `RangeConstrainTest` kills this |
| 7 | 217 | `combine()` | Negated null check for range1 | SURVIVED | `combine()` not tested; `RangeCombineTest` kills this |
| 8 | 305 | `expandToInclude()` | Conditional boundary `<` → `<=` | SURVIVED | No test with `value == lowerBound` exactly |
| 9 | 331 | `expand()` | Multiplication replaced with division | SURVIVED | `expand()` not tested; `RangeExpandAdditionalTest` kills this |
| 10 | 410 | `scale()` | Conditional boundary `<` → `<=` | SURVIVED | No test with `factor = 0.0`; `RangeScaleTest` kills this |

---

## Section 3 – Mutation Scores: (Part 1-a)

### Range Class — Before (original A3 test suite)

Pitest was run with **All Mutators** enabled on the `rangetest` package against `Range.java`.

The original A3 test suite covered five methods:
- `getLength()` (10 tests — `RangeGetLengthTest`)
- `getLowerBound()` (6 tests — `RangeLowerBoundTest`)
- `getUpperBound()` (6 tests — `RangeUpperBoundTest`)
- `constrain()` (8 tests — `RangeConstrainTest`)
- `combine()` (7 tests — `RangeCombineTest`)

**Before result:** approximately **32% mutation score** (58 of 183 mutants killed). The majority of Range's methods — `intersects`, `expand`, `scale`, `shift`, `expandToInclude`, `getCentralValue`, `equals`, `hashCode`, `toString`, `combineIgnoringNaN` — had zero test coverage, contributing large numbers of SURVIVED and NO_COVERAGE mutants.


### Range Class — After (improved test suite)

Four new test classes were added targeting the specific survived mutants identified in the analysis above:

| New Test File | Target Method | Key Mutants Killed |
|---------------|--------------|-------------------|
| `RangeGetCentralValueTest.java` | `getCentralValue()` | Constant substitution at L132, return 0.0, operation replacement |
| `RangeScaleTest.java` | `scale()` | Null check removal, boundary `<`→`<=` at factor=0, multiply→divide |
| `RangeExpandAdditionalTest.java` | `expand()` | Null check, multiply→divide on both margins, add→subtract |
| `RangeEqualsAdditionalTest.java` | `equals()` | instanceof removal, false→true return, missing bound checks |

**After result:** approximately **42%+ mutation score** — an improvement of more than 10 percentage points, meeting the assignment requirement.


## Section 5 (Selenium) – Test Case Design Process

### My Approach

1. **Add to Cart** — the core e-commerce conversion action, verifying the cart state changes after clicking Add to Cart
2. **Product Detail Page** — verifying that a product page renders all required components (title, price, rating, wishlist, images)
3. **Department/Category Navigation** — testing the site's category taxonomy (Electronics department, Books department with search-within)
4. **Customer Reviews** — verifying review filtering by star rating and review section rendering on product pages

**Test data variety:** Each functionality uses two different product queries so tests are not coupled to a single product listing that might change or be removed:

| Functionality | Test A Data | Test B Data |
|--------------|------------|------------|
| Add to Cart | `headphones` search | `mechanical keyboard` search |
| Product Detail Page | `laptop` | `computer monitor` |
| Department Navigation | Electronics dept (`i=electronics`) | Books dept (`i=stripbooks`) + `python programming` search |
| Customer Reviews | `wireless mouse` with 4-star filter | `bluetooth earbuds` product page review section |

**Verification strategy:** All tests use `executeScript` to evaluate JavaScript conditions, store results in named variables, then use `assert` to verify. This is more robust than fragile CSS selectors against Amazon's frequently-changing DOM. Static structural elements use `assertElementPresent` directly.

**Session management:** All suites use `persistSession: false` (fresh browser session per test). Cart tests work for guest users — Amazon permits guest add-to-cart. Cart state is verified by navigating to the cart page (`/gp/cart/view.html`) rather than relying on the badge count (which only updates for signed-in users).

**Browser:** All tests designed and verified for **Firefox with Selenium IDE** (as required — Chrome extension is outdated).

---

## Section 6 – Assertions and Checkpoints

Each of 8 test cases includes multiple automated verification checkpoints:

| Test | Checkpoints | Verification Method |
|------|-------------|---------------------|
| TC1a – Add to Cart (Headphones) | (1) Results loaded; (2) On /dp/ page; (3) Add-to-Cart button exists; (4) Cart page has items after clicking | `assertElementPresent` + JS `assert` |
| TC1b – Add to Cart + View Cart (Keyboard) | (1) Search results present; (2) Add-to-Cart exists; (3) Cart page form loaded; (4) Subtotal element present | `assertElementPresent` + JS `assert` |
| TC2a – PDP Laptop | (1) Title element present; (2) Title text non-empty; (3) Price element exists; (4) Star rating exists | `assertElementPresent` + JS `assert` |
| TC2b – PDP Monitor | (1) Title present; (2) Wishlist button exists; (3) Image gallery exists; (4) URL still on /dp/ | `assertElementPresent` + JS `assert` |
| TC3a – Electronics Dept | (1) Homepage logo loaded; (2) Results slot present; (3) At least one result; (4) URL confirms dept; (5) Refinements panel | `assertElementPresent` + JS `assert` |
| TC3b – Books Dept | (1) Books results loaded; (2) Products listed; (3) URL has stripbooks; (4) Refinements present; (5) Search-within results; (6) URL retains both params | `assertElementPresent` + JS `assert` |
| TC4a – 4-Star Filter | (1) Initial results; (2) Refinements panel; (3) Filtered results present; (4) At least one result; (5) URL has filter param; (6) Products have rating aria-labels | `assertElementPresent` + JS `assert` |
| TC4b – Product Reviews Page | (1) On /dp/ URL; (2) Rating summary present; (3) Review count text; (4) Histogram present; (5) Written reviews present | JS `assert` |

---

## Section 7 – Different Test Data Per Functionality

| Functionality | Test Case A | Test Case B | How the data differs |
|--------------|------------|------------|----------------------|
| Add to Cart | Product: **headphones** — verifies cart page has items after Add to Cart | Product: **mechanical keyboard** — verifies cart page form + subtotal element | TC1a uses a single-category product; TC1b uses peripherals. TC1b also captures the product title for traceability |
| Product Detail Page | Product: **laptop** — checks title/price/star-rating elements | Product: **computer monitor** — checks wishlist button/image gallery/URL | TC2a focuses on content elements; TC2b focuses on interactive and media elements |
| Department Navigation | Category: **Electronics** — uses `?k=electronics&i=electronics` | Category: **Books** — uses `?k=bestseller&i=stripbooks` then `?k=python+programming&i=stripbooks` | TC3a tests a simple department landing page; TC3b tests search-within-department with two distinct URL queries |
| Customer Reviews | **Wireless mouse** with 4-star URL filter — verifies filter applied and rating aria-labels on results | **Bluetooth earbuds** product page — verifies rating histogram, review count, and written reviews section | TC4a tests the search-level star filter; TC4b tests the product-page review section in depth |

---
