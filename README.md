# Description

A Java application that optimizes payment method allocation for a set of orders, considering discounts, limits, and mixed payment strategies.  
It selects the optimal combination of payment methods to minimize total cost and reduce card usage where possible.

---

# Features

- Fat JAR packaging
- Reads and parses orders and payment methods from JSON files
- Uses a lookahead strategy to evaluate combinations
- Gracefully handles invalid inputs and missing files
- Fully tested with JUnit 5

---

# Run

1. Download `Darya_Martyniuk_Java_Krakow.jar` from [Google Drive](https://drive.google.com/drive/u/0/folders/1uFDz_DwXKekzFeQ8RljLZ2HYjauXAUX1)
2. Open a terminal and navigate to the folder containing the JAR file:
   ```bash
   cd path/to/jar
   ```
3. Run the application:
   ```bash
   java -jar Darya_Martyniuk_Java_Krakow.jar path/to/orders.json path/to/paymentmethods.json
   ```

### Arguments

- `path/to/...jar` — path to the executable JAR file
- `orders.json` — path to a file containing the list of orders
- `paymentmethods.json` — path to a file containing available payment methods

### Example

```bash
java -jar /home/user/Darya_Martyniuk_Java_Krakow.jar /home/user/orders.json /home/user/paymentmethods.json
```

---

# Build

If downloading is not possible:

1. Clone this repository.
2. Make sure that [Maven](https://maven.apache.org/) is installed on your system.
3. In the project root directory, run:
   ```bash
   mvn clean package
   ```
4. The resulting file `Darya_Martyniuk_Java_Krakow.jar` will be located in the `target/` folder.

---

# Testing

1. Clone this repository.
2. In the project root directory, run:
   ```bash
   mvn test
   ```

Tests cover:
- Strategy selection and payment logic (`OptimizerTest`)
- JSON parsing and error handling (`JsonReaderTest`)

---

# Warnings

- Make sure both input files (`orders.json` and `paymentmethods.json`) exist and are properly formatted.
- The application requires **exactly two arguments**; incorrect usage will result in an error message.
- If input files are missing, unreadable, or malformed, the app will report:
  ```
  ❌ Failed to read input files: [error details]
  ```
- Common input issues:
  - Missing required fields (`id`, `value`, `promotions`)
  - Invalid JSON structure (e.g., trailing commas)
  - Negative `limit` or out-of-range `discount` values
  - Duplicate IDs

- `PUNKTY` discount is applied only when the full amount is paid with points.  
  For mixed payments (≥10% with points), a fixed 10% discount is applied.
