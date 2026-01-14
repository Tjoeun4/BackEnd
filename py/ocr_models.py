import numpy as np
import cv2
from paddleocr import PaddleOCR

class OCRHandler:
    def __init__(self):
        print("Loading PaddleOCR...")
        # 2026년 환경에 맞춰 mobile 대신 정확도가 높은 server 모델을 시도하거나 기본값을 사용합니다.
        self.paddle_ocr = PaddleOCR(use_angle_cls=True, lang='korean')

    def process_paddle(self, image_input):
        """
        image_input: 이미지 바이트 데이터 혹은 이미지 파일 경로
        """
        try:
            # 1. 입력이 바이트인 경우와 경로인 경우 모두 대응
            if isinstance(image_input, bytes):
                nparr = np.frombuffer(image_input, np.uint8)
                img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)
            else:
                img = cv2.imread(image_input)
            
            if img is None:
                print("이미지를 불러올 수 없습니다. 경로 혹은 파일 형식을 확인하세요.")
                return []

            # 2. 전처리: 너무 크거나 작으면 인식이 안 될 수 있으므로 크기 조정 (선택 사항)
            # h, w = img.shape[:2]
            # if w < 500: img = cv2.resize(img, (None, None), fx=2, fy=2, interpolation=cv2.INTER_CUBIC)

            # 3. OCR 실행
            result = self.paddle_ocr.ocr(img) 
            
            if not result or result[0] is None:
                return []
            
            final_results = []
            for line in result:
                for res in line:
                    # 데이터 구조: [ [ [x,y]좌표 ], (텍스트, 신뢰도) ]
                    text = res[1][0]
                    conf = float(res[1][1])
                    final_results.append({
                        "text": text,
                        "conf": conf
                    })
            
            return final_results
            
        except Exception as e:
            print(f"OCR 처리 중 에러 발생: {e}")
            return []