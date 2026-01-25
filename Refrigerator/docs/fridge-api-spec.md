# Fridge API 명세서

`/api/fridge` 하위의 모든 API 명세입니다.

- **Base URL**: `http://localhost:8080` (또는 서버 주소)
- **공통**: `Content-Type: application/json`, `Accept: application/json`
- **인증**: `/api/fridge/**`는 별도 JWT 불필요 (permitAll)

---

## 1. 식재료 추가 (Ingredients)

### 1-1. 식재료 매칭 (Resolve)

사용자가 입력한 이름으로 DB/AI 매칭 결과를 조회합니다.  
이 결과를 바탕으로 **Create** 요청 시 `itemAliasId` 또는 `itemId`를 사용합니다.

| 항목 | 내용 |
|------|------|
| **Method** | `POST` |
| **URL** | `/api/fridge/ingredients/resolve` |
| **요청 Body** | JSON |

**Request Body**

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| userId | Long | O | 사용자 ID |
| inputName | String | O | 사용자가 입력한 식재료 이름 (예: 양파, 두부) |

**Request 예시**
```json
{
  "userId": 1,
  "inputName": "양파"
}
```

**Response 200 OK**

| 필드 | 타입 | 설명 |
|------|------|------|
| type | String | `CONFIRM_ALIAS` \| `PICK_ITEM` \| `AI_PENDING` |
| aliasCandidate | Object | type=`CONFIRM_ALIAS`일 때만. Create 시 `itemAliasId` 사용 |
| itemCandidates | Array | type=`PICK_ITEM`일 때만. Create 시 그중 하나의 `itemId` 사용 |
| message | String | type=`AI_PENDING`일 때만. Create 시 `itemAliasId`, `itemId` 없이 요청 (AI 생성) |

**aliasCandidate**

| 필드 | 타입 |
|------|------|
| itemAliasId | Long |
| rawName | String |
| itemId | Long |
| itemName | String |

**itemCandidates[]**

| 필드 | 타입 |
|------|------|
| itemId | Long |
| itemName | String |
| expirationNum | Long |

**Response 예시**
```json
{
  "type": "PICK_ITEM",
  "aliasCandidate": null,
  "itemCandidates": [
    { "itemId": 5, "itemName": "양파", "expirationNum": 14 }
  ],
  "message": null
}
```

---

### 1-2. 식재료 냉장고 추가 (Create)

Resolve 결과를 반영하거나, AI 생성으로 냉장고에 식재료를 추가합니다.

| 항목 | 내용 |
|------|------|
| **Method** | `POST` |
| **URL** | `/api/fridge/ingredients` |
| **요청 Body** | JSON |

**Request Body**

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| userId | Long | O | 사용자 ID |
| inputName | String | O | 사용자가 입력한 원문 (Resolve에서 쓰인 값과 동일 권장) |
| itemAliasId | Long | - | Resolve `CONFIRM_ALIAS`일 때 사용. 있으면 itemId 무시 |
| itemId | Long | - | Resolve `PICK_ITEM`일 때 후보 중 선택한 ID. AI 생성 시 둘 다 생략 |
| quantity | Number | - | 수량 (예: 2, 0.5) |
| unit | String | - | 단위 (예: 개, g, ml) |
| purchaseDate | String | - | 구매일 `yyyy-MM-dd` |

**Request 예시 (PICK_ITEM 선택)**
```json
{
  "userId": 1,
  "inputName": "양파",
  "itemId": 5,
  "quantity": 2,
  "unit": "개",
  "purchaseDate": "2025-01-20"
}
```

**Request 예시 (AI 생성: itemAliasId, itemId 없음)**
```json
{
  "userId": 1,
  "inputName": "새재료",
  "quantity": 1,
  "unit": "개",
  "purchaseDate": "2025-01-20"
}
```

**Response 200 OK**

| 필드 | 타입 | 설명 |
|------|------|------|
| fridgeItemId | Long | 생성된 냉장고 품목 ID |
| itemId | Long | 표준 식재료(Items) ID |
| itemName | String | 표준 식재료명 |
| rawName | String | 냉장고에 저장된 표기명 (사용자 입력/alias) |
| purchaseDate | String | 구매일 `yyyy-MM-dd` |
| expiryDate | String | 유통기한 `yyyy-MM-dd` (purchaseDate + expirationNum 기반) |

**Response 예시**
```json
{
  "fridgeItemId": 101,
  "itemId": 5,
  "itemName": "양파",
  "rawName": "양파",
  "purchaseDate": "2025-01-20",
  "expiryDate": "2025-02-03"
}
```

**에러**

| HTTP | 예시 메시지 | 설명 |
|------|-------------|------|
| 404 | User not found: {id} | userId에 해당 사용자 없음 |
| 404 | ItemAlias not found / Item not found | 잘못된 itemAliasId 또는 itemId |
| 500 | AI itemName 비어있음 / expirationDays 비정상 | AI 추론 결과 이상 |

---

## 2. 팬트리 (Pantry)

기본 재료(마늘, 고추장, 설탕, 소금, 식용유)를 관리합니다.  
최초 조회/추천 시 비어 있으면 기본 5개가 자동 생성됩니다.

### 2-1. 팬트리 목록 조회

| 항목 | 내용 |
|------|------|
| **Method** | `GET` |
| **URL** | `/api/fridge/pantry` |
| **Query** | `userId` (Long, 필수) |

**Request 예시**
```
GET /api/fridge/pantry?userId=1
```

**Response 200 OK**

`PantryItemDto[]`

| 필드 | 타입 | 설명 |
|------|------|------|
| pantryItemId | Long | 팬트리 항목 ID |
| itemName | String | 재료명 |

**Response 예시**
```json
[
  { "pantryItemId": 1, "itemName": "마늘" },
  { "pantryItemId": 2, "itemName": "고추장" },
  { "pantryItemId": 3, "itemName": "설탕" }
]
```

---

### 2-2. 팬트리 재료 추가

| 항목 | 내용 |
|------|------|
| **Method** | `POST` |
| **URL** | `/api/fridge/pantry` |
| **Query** | `userId` (Long, 필수) |
| **요청 Body** | JSON |

**Request Body**

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| itemName | String | O | 추가할 재료명 |

**Request 예시**
```
POST /api/fridge/pantry?userId=1
Content-Type: application/json

{
  "itemName": "된장"
}
```

**Response 200 OK**
```json
{ "ok": true }
```

**Response 400 Bad Request** (itemName 누락/공백)
```json
{
  "ok": false,
  "message": "itemName is required"
}
```

---

### 2-3. 팬트리 재료 삭제

| 항목 | 내용 |
|------|------|
| **Method** | `DELETE` |
| **URL** | `/api/fridge/pantry/{pantryItemId}` |
| **Path** | `pantryItemId` (Long, 필수) |

**Request 예시**
```
DELETE /api/fridge/pantry/3
```

**Response 200 OK**
```json
{ "ok": true }
```

**에러**

| HTTP | 예시 메시지 | 설명 |
|------|-------------|------|
| 400 | PANTRY_ITEM_NOT_FOUND | 해당 pantryItemId 없음 |

---

## 3. 음식 추천 (Recommend)

**냉장고(FridgeItem) + 팬트리(PantryItem)만** 사용해 요리 3개를 추천합니다.  
허용된 재료 이외 사용 시 검증 실패 시 500을 반환할 수 있습니다.

### 3-1. 음식 추천 (3건)

| 항목 | 내용 |
|------|------|
| **Method** | `GET` |
| **URL** | `/api/fridge/recommend` |
| **Query** | `userId` (Long, 필수) |

**Request 예시**
```
GET /api/fridge/recommend?userId=1
```

**Response 200 OK**

| 필드 | 타입 | 설명 |
|------|------|------|
| userId | Long | 요청한 사용자 ID |
| recipes | Array | 추천 요리 3건 |

**recipes[] (Recipe)**

| 필드 | 타입 | 설명 |
|------|------|------|
| title | String | 요리명 |
| summary | String | 한 줄 설명 |
| timeMinutes | Integer | 예상 조리 시간(분) |
| difficulty | String | 난이도 (예: EASY, MEDIUM, HARD) |
| ingredients | String[] | 사용 재료 목록 (팬트리+냉장고 내) |
| steps | String[] | 조리 단계 |
| photoUrl | String | 음식 이미지 URL (플레이스홀더) |

**Response 예시**
```json
{
  "userId": 1,
  "recipes": [
    {
      "title": "양파달걀말이",
      "summary": "양파와 계란, 팬트리 재료로 만드는 간단 반찬",
      "timeMinutes": 15,
      "difficulty": "EASY",
      "ingredients": ["양파", "계란", "마늘", "식용유", "소금"],
      "steps": ["양파를 잘게 썬다.", "계란을 풀고 소금, 마늘을 넣고 섞는다.", "달군 팬에 식용유를 두르고 부친다."],
      "photoUrl": "https://picsum.photos/seed/12345/400/300"
    },
    { "title": "...", "summary": "...", "timeMinutes": 20, "difficulty": "MEDIUM", "ingredients": ["..."], "steps": ["..."], "photoUrl": "..." },
    { "title": "...", "summary": "...", "timeMinutes": 25, "difficulty": "EASY", "ingredients": ["..."], "steps": ["..."], "photoUrl": "..." }
  ]
}
```

**에러**

| HTTP | 예시 메시지 | 설명 |
|------|-------------|------|
| 400 | userId is required | query `userId` 누락 |
| 400 | 냉장고에 재료가 없으면 추천할 수 없습니다. | 해당 userId의 ACTIVE 냉장고 재료 0건 |
| 500 | Gemini generateText failed / Gemini HTTP... | Gemini API 키/네트워크 오류 |
| 500 | AI 추천이 규칙을 위반했습니다. (허용 재료 외 사용/형식 오류) | AI가 팬트리+냉장고 외 재료 사용 또는 형식 오류 |

---

## 4. API 목록 요약

| No | Method | Path | 설명 |
|----|--------|------|------|
| 1 | POST | /api/fridge/ingredients/resolve | 식재료 매칭 (alias/item/AI 판별) |
| 2 | POST | /api/fridge/ingredients | 냉장고 식재료 추가 |
| 3 | GET | /api/fridge/pantry | 팬트리 목록 (없으면 기본 5개 시딩) |
| 4 | POST | /api/fridge/pantry | 팬트리 재료 추가 |
| 5 | DELETE | /api/fridge/pantry/{pantryItemId} | 팬트리 재료 삭제 |
| 6 | GET | /api/fridge/recommend | 냉장고+팬트리 기반 요리 3건 추천 |

---

## 5. 공통 에러 형식

- `GlobalExceptionHandler` 기준:
  - **IllegalArgumentException**: 400, body에 `message` 문자열
  - **EntityNotFoundException**: 404, body에 `message` 문자열
  - **RuntimeException**: 400, `ErrorResponse` `{ success, message, code }`
  - **IllegalStateException**: 400, `ErrorResponse`

**ErrorResponse**
```json
{
  "success": false,
  "message": "에러 메시지",
  "code": "RUNTIME_EXCEPTION"
}
```
