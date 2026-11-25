import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: '',
        loadComponent: () =>
            import('./pages/home/home').then(m => m.Home)
    },
    {
        path: 'products',
        loadComponent: () =>
            import('./pages/products/products').then(m => m.Products)
    },
    {
        path: 'products/:id',
        loadComponent: () =>
            import('./pages/product-details/product-details')
                .then(m => m.ProductDetails)
    },
    {
        path: '**',
        redirectTo: ''
    }
];
