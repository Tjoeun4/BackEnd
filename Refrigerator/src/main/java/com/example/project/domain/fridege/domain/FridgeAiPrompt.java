package com.example.project.domain.fridege.domain;

import java.util.Map;

public class FridgeAiPrompt {

    public static String build(Map<String, Object> payload) {
    	return """
    	        너는 냉장고 식재료로만 요리 3가지를 추천하는 AI야.

    	        하드 규칙(절대 위반 금지):
    	        1) 사용 가능한 재료는 입력 JSON의 fridge 목록 + pantry(기본 조미료) 뿐이다.
    	           - 이 목록에 없는 재료는 절대 사용 금지.
    	        2) expiry_days가 작은(유통기한 임박) 재료를 우선 사용해.
    	           - expiry_days가 null이면 임박 판단에서 제외한다.
    	        3) 추가 구매는 절대 금지.
    	           - extra_purchase는 반드시 빈 배열 [] 로만 출력한다.
    	        4) 3가지 메뉴는 서로 너무 비슷하지 않게 구성한다.
    	           - 예: 볶음/볶음/볶음 같은 조리법 3연타 금지.
    	        5) pantry는 "기본 조미료는 항상 있다"는 가정이다.
    	           - pantry 재료 사용은 허용이며, 추가 구매로 치지 않는다.

    	        출력은 반드시 JSON만 반환(설명/코드블록/문장 금지).
    	        아래 형식 정확히 준수:
    	        {
    	          "recommendations": [
    	            {
    	              "title": "메뉴명",
    	              "use_items": ["사용한 재료들(반드시 fridge 또는 pantry 안에서만)"],
    	              "steps": ["조리 순서 문장", "조리 순서 문장"],
    	              "estimated_minutes": 15,
    	              "urgent_items_used": ["유통기한 임박 재료 중 실제 사용한 것"],
    	              "extra_purchase": []
    	            },
    	            { ... },
    	            { ... }
    	          ]
    	        }

    	        입력 JSON:
    	        %s
    	        """.formatted(payload);
    }
}
