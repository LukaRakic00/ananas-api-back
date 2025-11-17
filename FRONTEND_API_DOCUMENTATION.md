# API Dokumentacija za Frontend

## Base URL
```
http://localhost:8080/api/excel
```

## Endpointi sa CRUD privilegijama (za Frontend)

### 1. Upload Excel fajla
**POST** `/api/excel/upload`

Uploaduje Excel fajl (.xlsx ili .xls) i učitava podatke u bazu.

**Request:**
- Content-Type: `multipart/form-data`
- Body: `file` (Excel fajl)

**Response:**
```json
{
  "uploadId": "uuid",
  "totalRows": 100,
  "savedRows": 95,
  "message": "Uspešno učitano 95 redova",
  "rows": [
    {
      "id": 1,
      "uploadId": "uuid",
      "naziv": "Naziv reda",
      "vrednost": "Vrednost",
      "napomena": "Napomena",
      "rowNumber": 1,
      "createdAt": "2024-01-01T10:00:00",
      "updatedAt": "2024-01-01T10:00:00"
    }
  ]
}
```

**Primer korišćenja (JavaScript):**
```javascript
const formData = new FormData();
formData.append('file', fileInput.files[0]);

fetch('http://localhost:8080/api/excel/upload', {
  method: 'POST',
  body: formData
})
.then(response => response.json())
.then(data => console.log(data));
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
      "uploadId": "uuid",
      "naziv": "Naziv",
      "vrednost": "Vrednost",
      "napomena": "Napomena",
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

### 3. Pregled reda po ID-u
**GET** `/api/excel/{id}`

**Response:**
```json
{
  "id": 1,
  "uploadId": "uuid",
  "naziv": "Naziv",
  "vrednost": "Vrednost",
  "napomena": "Napomena",
  "rowNumber": 1,
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

---

### 4. Pregled redova po upload ID-u
**GET** `/api/excel/upload/{uploadId}`

**Response:**
```json
[
  {
    "id": 1,
    "uploadId": "uuid",
    "naziv": "Naziv",
    "vrednost": "Vrednost",
    "napomena": "Napomena",
    "rowNumber": 1,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
]
```

---

### 5. Pretraga (POST)
**POST** `/api/excel/search`

**Request Body:**
```json
{
  "search": "tekst za pretragu",
  "naziv": "naziv filter",
  "vrednost": "vrednost filter",
  "napomena": "napomena filter",
  "page": 0,
  "size": 20
}
```

**Response:** Ista struktura kao GET `/api/excel`

**Napomena:** Možete koristiti ili `search` (pretraga po svim poljima) ili specifične filtere (`naziv`, `vrednost`, `napomena`).

---

### 6. Pretraga (GET)
**GET** `/api/excel/search?search=tekst&page=0&size=20`

**Query parametri:**
- `search` - pretraga po svim poljima
- `naziv` - filter po nazivu
- `vrednost` - filter po vrednosti
- `napomena` - filter po napomeni
- `page` (default: 0)
- `size` (default: 20)

---

### 7. Kreiranje novog reda
**POST** `/api/excel`

**Request Body:**
```json
{
  "naziv": "Naziv reda",
  "vrednost": "Vrednost",
  "napomena": "Napomena",
  "rowNumber": 1
}
```

**Response:** Kreirani red (sa ID-em i timestamp-ovima)

---

### 8. Ažuriranje reda
**PUT** `/api/excel/{id}`

**Request Body:**
```json
{
  "naziv": "Ažurirani naziv",
  "vrednost": "Ažurirana vrednost",
  "napomena": "Ažurirana napomena",
  "rowNumber": 1
}
```

**Response:** Ažurirani red

---

### 9. Brisanje reda
**DELETE** `/api/excel/{id}`

**Response:** 204 No Content

---

### 10. Export kao XML (svi redovi)
**GET** `/api/excel/export/xml?page=0&size=1000`

**Response:** XML fajl (Content-Type: application/xml)

**Primer korišćenja:**
```javascript
fetch('http://localhost:8080/api/excel/export/xml?page=0&size=1000')
  .then(response => response.text())
  .then(xml => {
    // Download XML fajl
    const blob = new Blob([xml], { type: 'application/xml' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'excel_rows.xml';
    a.click();
  });
```

---

### 11. Export kao XML (po upload ID-u)
**GET** `/api/excel/export/xml/{uploadId}`

**Response:** XML fajl sa redovima za određeni upload

---

## Endpointi sa READ privilegijama (za Ananas)

Base URL: `http://localhost:8080/api/ananas/excel`

Svi endpointi su isti kao gore, ali **NEMA** POST, PUT, DELETE metoda. Samo GET metode:

- `GET /api/ananas/excel` - pregled svih redova
- `GET /api/ananas/excel/{id}` - pregled po ID-u
- `GET /api/ananas/excel/upload/{uploadId}` - pregled po upload ID-u
- `GET /api/ananas/excel/search?search=tekst` - pretraga
- `GET /api/ananas/excel/filter?naziv=...&vrednost=...` - filter
- `GET /api/ananas/excel/export/xml` - export XML
- `GET /api/ananas/excel/export/xml/{uploadId}` - export XML po upload ID-u

---

## Format Excel fajla

Excel fajl treba da ima sledeću strukturu:
- **Prva kolona (A)**: Naziv (obavezno)
- **Druga kolona (B)**: Vrednost (opciono)
- **Treća kolona (C)**: Napomena (opciono)

Prvi red se preskače (header).

---

## Error Handling

Svi endpointi vraćaju standardne HTTP status kodove:
- `200 OK` - uspešan zahtev
- `201 Created` - uspešno kreiranje
- `204 No Content` - uspešno brisanje
- `400 Bad Request` - greška u zahtevu
- `404 Not Found` - resurs nije pronađen
- `500 Internal Server Error` - server greška

---

## Primer kompletnog React komponenta

```jsx
import React, { useState } from 'react';

function ExcelUpload() {
  const [file, setFile] = useState(null);
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleUpload = async () => {
    if (!file) return;
    
    setLoading(true);
    const formData = new FormData();
    formData.append('file', file);

    try {
      const response = await fetch('http://localhost:8080/api/excel/upload', {
        method: 'POST',
        body: formData
      });
      const result = await response.json();
      setData(result);
    } catch (error) {
      console.error('Error:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <input type="file" accept=".xlsx,.xls" onChange={(e) => setFile(e.target.files[0])} />
      <button onClick={handleUpload} disabled={loading}>
        {loading ? 'Uploading...' : 'Upload'}
      </button>
      {data && <pre>{JSON.stringify(data, null, 2)}</pre>}
    </div>
  );
}

export default ExcelUpload;
```

