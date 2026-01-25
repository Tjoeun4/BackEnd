package com.example.project.domain.fridge.prompt;

public class InferItemPrompt {

    private InferItemPrompt() {}

    public static String build(String userInputName) {
        return """
            너는 식재료 DB 구축 도우미야.
            사용자가 입력한 식재료 이름을 표준화해서 items 테이블에 넣을 정보를 만든다.

            아래 JSON만 출력해. 다른 텍스트/설명/코드블록/마크다운 금지.
            JSON 스키마:
            {
              "itemName": "표준식재료명(한국어, 2~20자)",
              "expirationDays": 1~365 정수,
              "categoryName": "육류|양념|채소|유제품|해산물|과일 중 하나",
              "confidence": 0.0~1.0 숫자,
              "notes": "가정/근거 1줄"
            }

            categoryName은 반드시 다음 6개 중 정확히 하나만 사용:
            [육류, 양념, 채소, 유제품, 해산물, 과일]

            사용자 입력: "%s"
            """.formatted(userInputName);
    }
}
