# Dokumentacija za Frontend - Merchant Inventory API

## 🌐 Base URL

**Produkcija:**
```
https://ananas-api-back.onrender.com/api/excel
```

**Lokalno razvojno okruženje:**
```
http://localhost:8080/api/excel
```

---

## 📋 Struktura Podataka

### MerchantInventoryDTO

Svaki red u tabeli ima sledeće kolone (tačno kako treba da se prikažu na frontu):

| Kolona (Frontend) | API Field | Tip | Opis |
|-------------------|-----------|-----|------|
| **Merchant Inventory ID** | `merchantInventoryId` | String | ID inventara |
| **Ime proizvoda** | `productName` | String | Naziv proizvoda (obavezno) |
| **Status objave** | `status` | String | Status proizvoda |
| **Kategorija** | `l1Category` | String | L1 kategorija |
| **Tip proizvoda** | `productType` | String | Tip proizvoda |
| **EAN** | `ean` | String | EAN kod |
| **ā kod** | `aCode` | String | A kod |
| **SKU** | `sku` | String | SKU kod |
| **Tagovi** | `tags` | String | Tagovi proizvoda |
| **Skladište** | `warehouse` | String | Naziv skladišta |
| **Količina** | `currentStock` | Integer | Trenutna količina na stanju |
| **Cena** | `basePriceWithVat` | Decimal | Osnovna cena sa PDV-om |
| **Akcijska cena** | `newBasePriceWithVat` | Decimal | Nova osnovna cena sa PDV-om |
| **PDV (%)** | `vat` | Decimal | PDV procenat |
| **Novi PDV (%)** | `newVat` | Decimal | Novi PDV procenat |

**Dodatna polja (ne prikazivati u tabeli):**
- `id` - Interni ID
- `uploadId` - ID upload-a
- `rowNumber` - Broj reda u Excel fajlu
- `createdAt` - Datum kreiranja
- `updatedAt` - Datum ažuriranja

---

## 🔍 Endpointi

### 1. Upload Excel fajla
**POST** `/api/excel/upload`

**Request:**
- Method: `POST`
- Content-Type: `multipart/form-data`
- Body: `file` (Excel fajl)

**Excel Format:**s
Excel fajl mora imati tačno ovaj redosled kolona (prvi red je header):
1. MerchantInventoryId
2. Product name
3. Status
4. L1 Category
5. Product type
6. EAN
7. a code
8. SKU
9. Tags
10. Warehouse
11. Current stock
12. Base price with VAT
13. New base price with VAT
14. VAT
15. New VAT

**Response:**
```json
{
  "uploadId": "uuid",
  "totalRows": 100,
  "savedRows": 95,
  "message": "Uspešno učitano 95 redova",
  "rows": [...]
}
```

---

### 2. Pregled svih redova (sa paginacijom)
**GET** `/api/excel?page=0&size=20`

**Query parametri:**
- `page` (default: 0) - broj stranice
- `size` (default: 20) - broj elemenata po stranici

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "merchantInventoryId": "INV001",
      "productName": "Proizvod 1",
      "status": "Aktivan",
      "l1Category": "Elektronika",
      "productType": "Mobilni telefon",
      "ean": "1234567890123",
      "aCode": "A001",
      "sku": "SKU001",
      "tags": "tag1, tag2",
      "warehouse": "Skladište 1",
      "currentStock": 50,
      "basePriceWithVat": 10000.00,
      "newBasePriceWithVat": 9500.00,
      "vat": 20.00,
      "newVat": 20.00,
      "rowNumber": 1,
      "createdAt": "2024-01-01T10:00:00",
      "updatedAt": "2024-01-01T10:00:00"
    }
  ],
  "totalElements": 100,
  "totalPages": 5,
  "size": 20,
  "number": 0
}
```

---

### 3. Pretraga sa filterima (POST)
**POST** `/api/excel/search`

**Request Body:**
```json
{
  "search": "tekst za pretragu",
  "merchantInventoryId": "INV001",
  "productName": "Proizvod",
  "status": "Aktivan",
  "l1Category": "Elektronika",
  "productType": "Mobilni telefon",
  "ean": "1234567890123",
  "aCode": "A001",
  "sku": "SKU001",
  "tags": "tag1",
  "warehouse": "Skladište 1",
  "currentStockMin": 10,
  "currentStockMax": 100,
  "page": 0,
  "size": 20
}
```

**Napomena:** 
- `search` - pretraga po svim tekstualnim poljima (productName, merchantInventoryId, ean, sku, aCode, tags)
- Specifični filteri - filtriranje po određenim kolonama
- `currentStockMin` i `currentStockMax` - filtriranje po opsegu količine

---

### 4. Pretraga sa filterima (GET)
**GET** `/api/excel/search?productName=Proizvod&status=Aktivan&currentStockMin=10&currentStockMax=100&page=0&size=20`

**Query parametri:**
- `search` - pretraga po svim tekstualnim poljima
- `merchantInventoryId` - filter po Merchant Inventory ID
- `productName` - filter po nazivu proizvoda
- `status` - filter po statusu
- `l1Category` - filter po kategoriji
- `productType` - filter po tipu proizvoda
- `ean` - filter po EAN kodu
- `aCode` - filter po A kodu
- `sku` - filter po SKU kodu
- `tags` - filter po tagovima
- `warehouse` - filter po skladištu
- `currentStockMin` - minimalna količina
- `currentStockMax` - maksimalna količina
- `page` (default: 0)
- `size` (default: 20)

---

### 5. Kreiranje novog reda
**POST** `/api/excel`

**Request Body:**
```json
{
  "merchantInventoryId": "INV001",
  "productName": "Novi proizvod",
  "status": "Aktivan",
  "l1Category": "Elektronika",
  "productType": "Mobilni telefon",
  "ean": "1234567890123",
  "aCode": "A001",
  "sku": "SKU001",
  "tags": "tag1, tag2",
  "warehouse": "Skladište 1",
  "currentStock": 50,
  "basePriceWithVat": 10000.00,
  "newBasePriceWithVat": 9500.00,
  "vat": 20.00,
  "newVat": 20.00
}
```

---

### 6. Ažuriranje reda
**PUT** `/api/excel/{id}`

**Request Body:** Isti format kao za kreiranje

---

### 7. Brisanje reda
**DELETE** `/api/excel/{id}`

**Response:** 204 No Content

---

### 8. Brisanje svih redova
**DELETE** `/api/excel`

**Response:**
```json
{
  "message": "Svi redovi su uspešno obrisani"
}
```

---

### 9. Export kao XML
**GET** `/api/excel/export/xml?page=0&size=1000`

**Response:** XML fajl

---

## 🎨 Primeri Prikaza na Frontu

### Tabela sa kolonama (prema slikama)

```jsx
const columns = [
  { field: 'merchantInventoryId', headerName: 'Merchant Inventory ID', width: 150 },
  { field: 'productName', headerName: 'Ime proizvoda', width: 200, sortable: true },
  { field: 'ean', headerName: 'EAN', width: 120 },
  { field: 'aCode', headerName: 'ā kod', width: 100 },
  { field: 'sku', headerName: 'SKU', width: 120 },
  { field: 'tags', headerName: 'Tagovi', width: 150 },
  { field: 'status', headerName: 'Status objave', width: 130 },
  { field: 'warehouse', headerName: 'Skladište', width: 120 },
  { field: 'l1Category', headerName: 'Kategorija', width: 120 },
  { field: 'productType', headerName: 'Tip proizvoda', width: 150 },
  { field: 'basePriceWithVat', headerName: 'Cena', width: 100, sortable: true, type: 'number' },
  { field: 'currentStock', headerName: 'Količina', width: 100, sortable: true, type: 'number' },
  { field: 'newBasePriceWithVat', headerName: 'Akcijska cena', width: 130 },
  { field: 'vat', headerName: 'PDV (%)', width: 100 },
  { field: 'newVat', headerName: 'Novi PDV (%)', width: 120 }
];
```

### Filteri po kolonama

```jsx
// Primer filter komponente
const filters = {
  productName: '',
  status: '',
  l1Category: '',
  productType: '',
  ean: '',
  sku: '',
  warehouse: '',
  currentStockMin: null,
  currentStockMax: null
};

// GET request sa filterima
const searchParams = new URLSearchParams();
Object.entries(filters).forEach(([key, value]) => {
  if (value !== null && value !== '') {
    searchParams.append(key, value);
  }
});

fetch(`https://ananas-api-back.onrender.com/api/excel/search?${searchParams.toString()}`)
```

---

## 📝 Formatiranje Vrednosti

### Cene (Decimal)
```javascript
const formatPrice = (price) => {
  if (!price) return '-';
  return new Intl.NumberFormat('sr-RS', {
    style: 'currency',
    currency: 'RSD'
  }).format(price);
};
```

### PDV (Decimal)
```javascript
const formatVAT = (vat) => {
  if (!vat) return '-';
  return `${vat}%`;
};
```

### Količina (Integer)
```javascript
const formatStock = (stock) => {
  if (stock === null || stock === undefined) return '-';
  return stock.toString();
};
```

---

## 🔄 Primer Kompletnog React Komponenta

```jsx
import React, { useState, useEffect } from 'react';

function MerchantInventoryTable() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(20);
  const [filters, setFilters] = useState({});

  const fetchData = async () => {
    setLoading(true);
    try {
      const params = new URLSearchParams({
        page: page.toString(),
        size: size.toString(),
        ...filters
      });
      
      const response = await fetch(
        `https://ananas-api-back.onrender.com/api/excel/search?${params}`
      );
      const result = await response.json();
      setData(result);
    } catch (error) {
      console.error('Error:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [page, size, filters]);

  return (
    <div>
      {/* Filter komponenta */}
      <div className="filters">
        <input
          placeholder="Ime proizvoda"
          onChange={(e) => setFilters({...filters, productName: e.target.value})}
        />
        <input
          placeholder="Status"
          onChange={(e) => setFilters({...filters, status: e.target.value})}
        />
        {/* Dodajte ostale filtere */}
      </div>

      {/* Tabela */}
      <table>
        <thead>
          <tr>
            <th>Ime proizvoda</th>
            <th>EAN</th>
            <th>ā kod</th>
            <th>SKU</th>
            <th>Status objave</th>
            <th>Skladište</th>
            <th>Kategorija</th>
            <th>Cena</th>
            <th>Količina</th>
            {/* Dodajte ostale kolone */}
          </tr>
        </thead>
        <tbody>
          {data.content?.map((row) => (
            <tr key={row.id}>
              <td>{row.productName}</td>
              <td>{row.ean}</td>
              <td>{row.aCode}</td>
              <td>{row.sku}</td>
              <td>{row.status}</td>
              <td>{row.warehouse}</td>
              <td>{row.l1Category}</td>
              <td>{row.basePriceWithVat?.toFixed(2)}</td>
              <td>{row.currentStock}</td>
              {/* Dodajte ostale kolone */}
            </tr>
          ))}
        </tbody>
      </table>

      {/* Paginacija */}
      <div className="pagination">
        <button onClick={() => setPage(page - 1)} disabled={page === 0}>
          Prethodna
        </button>
        <span>Stranica {page + 1} od {data.totalPages}</span>
        <button 
          onClick={() => setPage(page + 1)} 
          disabled={page >= data.totalPages - 1}
        >
          Sledeća
        </button>
      </div>
    </div>
  );
}

export default MerchantInventoryTable;
```

---

## ⚠️ Važne Napomene

1. **Obavezno polje:** `productName` - mora biti popunjeno
2. **Sortiranje:** Kolone sa strelicama (kao na slikama) su sortabilne
3. **Filteri:** Svi filteri su opcioni, možete kombinovati više filtera
4. **Paginacija:** Uvek koristite paginaciju za velike skupove podataka
5. **Format Excel fajla:** Redosled kolona mora biti tačan kako je navedeno

---

## 🚀 Brzi Start

```javascript
// 1. Upload Excel fajla
const formData = new FormData();
formData.append('file', fileInput.files[0]);

const uploadResponse = await fetch(
  'https://ananas-api-back.onrender.com/api/excel/upload',
  { method: 'POST', body: formData }
);

// 2. Pregled podataka
const dataResponse = await fetch(
  'https://ananas-api-back.onrender.com/api/excel?page=0&size=20'
);
const data = await dataResponse.json();

// 3. Pretraga sa filterima
const searchResponse = await fetch(
  'https://ananas-api-back.onrender.com/api/excel/search?productName=Proizvod&status=Aktivan'
);
const searchData = await searchResponse.json();
```

