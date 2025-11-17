# API Endpoints - Brzi Reference

## 🌐 Produkcija

**Base URL:** `https://ananas-api-back.onrender.com`

### Frontend Endpointi (CRUD)
```
https://ananas-api-back.onrender.com/api/excel
```

### Ananas Endpointi (READ only)
```
https://ananas-api-back.onrender.com/api/ananas/excel
```

## 💻 Lokalno Razvojno Okruženje

**Base URL:** `http://localhost:8080`

### Frontend Endpointi (CRUD)
```
http://localhost:8080/api/excel
```

### Ananas Endpointi (READ only)
```
http://localhost:8080/api/ananas/excel
```

## 📋 Glavni Endpointi

### Upload Excel fajla
- **POST** `/api/excel/upload`

### Pregled svih redova
- **GET** `/api/excel?page=0&size=20`

### Pretraga
- **GET** `/api/excel/search?search=tekst`
- **POST** `/api/excel/search`

### CRUD Operacije
- **POST** `/api/excel` - Kreiranje
- **PUT** `/api/excel/{id}` - Ažuriranje
- **DELETE** `/api/excel/{id}` - Brisanje
- **DELETE** `/api/excel` - Brisanje svih

### Export
- **GET** `/api/excel/export/xml` - Export kao XML

## 🔗 Health Check

Proverite da li je API dostupan:

```bash
curl https://ananas-api-back.onrender.com/api/excel
```

