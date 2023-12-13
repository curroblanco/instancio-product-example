# Instancio Example - Product Creation

This repository demonstrates the implementation of the Mother testing pattern using the Instancio library for generating realistic product instances. The Mother testing pattern is a technique for creating a large number of test objects with randomized properties while maintaining some structure to ensure the realism of testing scenarios.

## Overview

The Instancio library provides a powerful framework for creating and customizing objects with randomized attributes. It allows for defining prototypes, which serve as blueprints for generating instances with various configurations. The Mother testing pattern leverages Instancio's capabilities to efficiently generate test products that exhibit realistic variability while maintaining consistency within a given context.

## Features
* **Randomized Product Creation:** Instancio enables the creation of products with randomized properties, ensuring a diverse range of test cases.
* **Structured Randomization:** The Mother testing pattern maintains a structured approach to randomization, ensuring that products adhere to expected patterns and relationships within a product catalog.
* **Efficient Product Generation:** Instancio's optimized algorithms for object creation streamline the process of generating large test suites with varying product attributes.
* **Customizable Prototypes:** Define prototypes to control the structure and properties of generated products, tailoring them to specific testing needs.

## Usage
To utilize Instancio for product testing, follow these steps:
1. **Define Product Prototypes:** Create prototypes for the products you want to generate. These prototypes define the structure and properties of the products.
2. **Initialize Product Managers:** Instancio provides product managers for managing the creation of products based on prototypes.
3. **Generate Products:** Use product managers to generate the desired number of products, each with randomized properties consistent with their respective prototypes.
4. **Execute Tests:** Employ the generated products in your testing procedures to thoroughly evaluate your code under various product scenarios.

## Examples
The provided example demonstrates the creation of product instances with randomized attributes, such as product ID, part number, first availability date, and publication date. It utilizes Instancio to generate a large number of product instances with varying combinations of these properties.

**Example 1: Generating a Product with random attributes**

```java
ProductManager<Product> productManager = Instancio.create(Product.class);
Product product = productManager.newInstance();

System.out.println("Product ID: " + product.getId());
System.out.println("Product part number: " + product.getPartNumber());
System.out.println("Product first availability date: " + product.getFirstAvailabilityDate());
System.out.println("Product publication date: " + product.getPublicationDate());
