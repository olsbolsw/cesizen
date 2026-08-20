# Angular API URL

For the Nginx reverse proxy to work cleanly, the browser should call the backend with a **relative URL**:

```ts
export const environment = {
  production: true,
  apiUrl: '/api'
};
```

Then services should build URLs from `environment.apiUrl`, for example:

```ts
this.http.get(`${environment.apiUrl}/articles/published`)
```

Avoid hardcoding `http://localhost:8081/api` in production code. When the browser calls `/api`, Nginx proxies it internally to `http://backend:8081`.
