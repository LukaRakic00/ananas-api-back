# Pregled API Endpointa

## 📋 `/api/excel` - CRUD Endpoint (za Frontend)

Ovaj endpoint ima **SVE CRUD operacije** (Create, Read, Update, Delete).

### CREATE (Kreiranje)
- ✅ `POST /api/excel/upload` - Upload Excel fajla
- ✅ `POST /api/excel` - Kreiranje novog reda

### READ (Čitanje)
- ✅ `GET /api/excel` - Pregled svih redova (JSON default, XML sa Accept header)
- ✅ `GET /api/excel/{id}` - Pregled reda po ID-u
- ✅ `GET /api/excel/upload/{uploadId}` - Pregled redova po upload ID-u
- ✅ `GET /api/excel/search` - Pretraga sa filterima (GET)
- ✅ `POST /api/excel/search` - Pretraga sa filterima (POST)

### UPDATE (Ažuriranje)
- ✅ `PUT /api/excel/{id}` - Ažuriranje reda

### DELETE (Brisanje)
- ✅ `DELETE /api/excel/{id}` - Brisanje reda
- ✅ `DELETE /api/excel` - Brisanje svih redova

### EXPORT
- ✅ `GET /api/excel/export/xml` - Export u XML format
- ✅ `GET /api/excel/export/xml/{uploadId}` - Export po upload ID u XML
- ✅ `GET /api/excel/export/excel` - Export u Excel format (.xlsx)
- ✅ `GET /api/excel/export/excel/{uploadId}` - Export po upload ID u Excel

---

## 📋 `/api/ananas/excel` - READ ONLY Endpoint (za Ananas)

Ovaj endpoint ima **SAMO READ operacije** (bez Create, Update, Delete).

### READ (Čitanje)
- ✅ `GET /api/ananas/excel` - Pregled svih redova
- ✅ `GET /api/ananas/excel/{id}` - Pregled reda po ID-u
- ✅ `GET /api/ananas/excel/upload/{uploadId}` - Pregled redova po upload ID-u
- ✅ `GET /api/ananas/excel/search` - Pretraga po bilo kom polju
- ✅ `GET /api/ananas/excel/filter` - Pretraga sa specifičnim filterima

### EXPORT (READ ONLY)
- ✅ `GET /api/ananas/excel/export/xml` - Export u XML format
- ✅ `GET /api/ananas/excel/export/xml/{uploadId}` - Export po upload ID u XML
- ✅ `GET /api/ananas/excel/export/excel` - Export u Excel format (.xlsx)
- ✅ `GET /api/ananas/excel/export/excel/{uploadId}` - Export po upload ID u Excel

---

## 🔒 Bezbednosne Napomene

- `/api/excel` endpoint ima **POTPUNE CRUD privilegije** - može kreirati, čitati, ažurirati i brisati podatke
- `/api/ananas/excel` endpoint ima **SAMO READ privilegije** - može samo čitati i eksportovati podatke
- Ananas endpoint **NEMA** mogućnost kreiranja, ažuriranja ili brisanja podataka

---

## 📝 Format Podataka

### Excel Upload Format
- Excel fajl mora imati header u **A1** redu
- **A2** red je prazan
- Podaci počinju od **A3** reda
- Redosled kolona:
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

### Excel Export Format
- Header u **A1** redu
- Prazan **A2** red
- Podaci počinju od **A3** reda
- Isti redosled kolona kao upload

### XML Format
- Podržan preko `Accept: application/xml` header-a
- Ili preko `/export/xml` endpointa

