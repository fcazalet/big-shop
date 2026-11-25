import { Injectable } from '@angular/core';
import { Product } from '../models/product.model';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private products: Product[] = [
    {
      id: 1,
      name: 'Laptop',
      description: 'Powerful laptop for everyday use.',
      price: 899,
      image: 'assets/images/laptop.jpg',
      category: 'Electronics',
      stock: 10,
      active: true
    },
    {
      id: 2,
      name: 'Smartphone',
      description: 'Latest generation smartphone.',
      price: 599,
      image: 'assets/images/smartphone.jpg',
      category: 'Electronics',
      stock: 20,
      active: true
    },
    {
      id: 3,
      name: 'Headphones',
      description: 'Wireless noise cancelling headphones.',
      price: 99,
      image: 'assets/images/headphones.jpg',
      category: 'Accessories',
      stock: 15,
      active: true
    }
  ];

  // Get all products
  getProducts(): Product[] {
    // return this.http.get<Product[]>('/api/products');
    return this.products;
  }

  // Get a product by ID
  getProductById(id: number): Product | undefined {
    // return this.http.get<Product>(`/api/products/${id}`);
    return this.products.find(product => product.id === id);
  }

  // Get active products
  getActiveProducts(): Product[] {
    return this.products.filter(product => product.active);
  }

  // Add a product
  addProduct(product: Product): void {
    this.products.push(product);
  }

  // Update a product
  updateProduct(updatedProduct: Product): void {
    const index = this.products.findIndex(
        product => product.id === updatedProduct.id
    );

    if (index !== -1) {
      this.products[index] = updatedProduct;
    }
  }

  // Delete a product
  deleteProduct(id: number): void {
    this.products = this.products.filter(product => product.id !== id);
  }
}
