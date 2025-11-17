# Brzi Start za Frontend Tim - Merchant Inventory

## 🎯 Šta je promenjeno

Stara tabela `excel_rows` je obrisana i zamenjena novom tabelom `merchant_inventory` sa 15 kolona.

## 📋 Kolone u Tabeli (tačno kako treba da se prikažu)

1. **Merchant Inventory ID** (`merchantInventoryId`)
2. **Ime proizvoda** (`productName`) - obavezno polje, sortabilno
3. **Status objave** (`status`)
4. **Kategorija** (`l1Category`)
5. **Tip proizvoda** (`productType`)
6. **EAN** (`ean`)
7. **ā kod** (`aCode`)
8. **SKU** (`sku`)
9. **Tagovi** (`tags`)
10. **Skladište** (`warehouse`)
11. **Količina** (`currentStock`) - sortabilno, tip: Integer
12. **Cena** (`basePriceWithVat`) - sortabilno, tip: Decimal
13. **Akcijska cena** (`newBasePriceWithVat`) - tip: Decimal
14. **PDV (%)** (`vat`) - tip: Decimal
15. **Novi PDV (%)** (`newVat`) - tip: Decimal

## 🔗 API Endpointi (ISTI kao pre)

**Base URL (Produkcija):** `https://ananas-api-back.onrender.com/api/excel`  
**Base URL (Lokalno):** `http://localhost:8080/api/excel`

### Glavni Endpointi:

- `POST /api/excel/upload` - Upload Excel fajla
- `GET /api/excel?page=0&size=20` - Pregled sa paginacijom
- `POST /api/excel/search` - Pretraga sa filterima (POST)
- `GET /api/excel/search?productName=...&status=...` - Pretraga sa filterima (GET)
- `POST /api/excel` - Kreiranje novog reda
- `PUT /api/excel/{id}` - Ažuriranje reda
- `DELETE /api/excel/{id}` - Brisanje reda
- `DELETE /api/excel` - Brisanje svih redova

## 🔍 Filteri po Kolonama

Sve kolone imaju filtere! Možete filtrirati po:

- `merchantInventoryId` - tekstualna pretraga
- `productName` - tekstualna pretraga
- `status` - tekstualna pretraga
- `l1Category` - tekstualna pretraga
- `productType` - tekstualna pretraga
- `ean` - tekstualna pretraga
- `aCode` - tekstualna pretraga
- `sku` - tekstualna pretraga
- `tags` - tekstualna pretraga
- `warehouse` - tekstualna pretraga
- `currentStockMin` / `currentStockMax` - opseg za količinu

## 📄 Format Excel Fajla

Excel fajl mora imati **tačno ovaj redosled kolona** (prvi red je header):

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

## 💡 Primer Response-a

```json
{
  "content": [
    {
      "id": 1,
      "merchantInventoryId": "INV001",
      "productName": "Samsung Galaxy S21",
      "status": "Aktivan",
      "l1Category": "Elektronika",
      "productType": "Mobilni telefon",
      "ean": "8806094567890",
      "aCode": "A001",
      "sku": "SKU-S21-001",
      "tags": "samsung, telefon, android",
      "warehouse": "Skladište Beograd",
      "currentStock": 50,
      "basePriceWithVat": 75000.00,
      "newBasePriceWithVat": 69999.00,
      "vat": 20.00,
      "newVat": 20.00
    }
  ],
  "totalElements": 100,
  "totalPages": 5,
  "size": 20,
  "number": 0
}
```

## 🎨 Primer Prikaza Tabele

```jsx
// Kolone za tabelu
const columns = [
  { field: 'productName', headerName: 'Ime proizvoda', sortable: true },
  { field: 'ean', headerName: 'EAN' },
  { field: 'aCode', headerName: 'ā kod' },
  { field: 'sku', headerName: 'SKU' },
  { field: 'tags', headerName: 'Tagovi' },
  { field: 'status', headerName: 'Status objave' },
  { field: 'warehouse', headerName: 'Skladište' },
  { field: 'l1Category', headerName: 'Kategorija' },
  { field: 'productType', headerName: 'Tip proizvoda' },
  { field: 'basePriceWithVat', headerName: 'Cena', sortable: true, type: 'number' },
  { field: 'currentStock', headerName: 'Količina', sortable: true, type: 'number' },
  { field: 'newBasePriceWithVat', headerName: 'Akcijska cena' },
  { field: 'vat', headerName: 'PDV (%)' },
  { field: 'newVat', headerName: 'Novi PDV (%)' }
];
```

## ⚠️ VAŽNO

1. **Obavezno polje:** `productName` - mora biti popunjeno u Excel fajlu
2. **Redosled kolona:** Excel fajl mora imati tačan redosled kolona
3. **Filteri:** Svi filteri su opcioni, možete kombinovati više filtera
4. **Sortiranje:** Kolone sa strelicama (kao na slikama) su sortabilne

## 📚 Detaljna Dokumentacija

Pogledajte `FRONTEND_MERCHANT_INVENTORY.md` za kompletnu dokumentaciju sa primerima koda.

