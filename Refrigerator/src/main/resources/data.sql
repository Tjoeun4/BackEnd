-- 1. 서울특별시 (11)
MERGE INTO neighborhoods n USING (SELECT 11110 id, '서울특별시' city, '종로구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 11140 id, '서울특별시' city, '중구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 11170 id, '서울특별시' city, '용산구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 11200 id, '서울특별시' city, '성동구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 11215 id, '서울특별시' city, '광진구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 11230 id, '서울특별시' city, '동대문구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 11260 id, '서울특별시' city, '중랑구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 11290 id, '서울특별시' city, '성북구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 11305 id, '서울특별시' city, '강북구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 11320 id, '서울특별시' city, '도봉구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 11350 id, '서울특별시' city, '노원구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 11380 id, '서울특별시' city, '은평구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 11410 id, '서울특별시' city, '서대문구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 11440 id, '서울특별시' city, '마포구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 11470 id, '서울특별시' city, '양천구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 11500 id, '서울특별시' city, '강서구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 11530 id, '서울특별시' city, '구로구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 11545 id, '서울특별시' city, '금천구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 11560 id, '서울특별시' city, '영등포구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 11590 id, '서울특별시' city, '동작구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 11620 id, '서울특별시' city, '관악구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 11650 id, '서울특별시' city, '서초구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 11680 id, '서울특별시' city, '강남구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 11710 id, '서울특별시' city, '송파구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 11740 id, '서울특별시' city, '강동구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);

-- 2. 부산광역시 (26)
MERGE INTO neighborhoods n USING (SELECT 26110 id, '부산광역시' city, '중구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 26140 id, '부산광역시' city, '서구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 26170 id, '부산광역시' city, '동구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 26200 id, '부산광역시' city, '영도구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 26230 id, '부산광역시' city, '부산진구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 26260 id, '부산광역시' city, '동래구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 26290 id, '부산광역시' city, '남구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 26320 id, '부산광역시' city, '북구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 26350 id, '부산광역시' city, '해운대구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 26380 id, '부산광역시' city, '사하구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 26410 id, '부산광역시' city, '금정구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 26440 id, '부산광역시' city, '강서구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 26470 id, '부산광역시' city, '연제구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 26500 id, '부산광역시' city, '수영구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 26530 id, '부산광역시' city, '사상구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 26710 id, '부산광역시' city, '기장군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);

-- 3. 대구광역시 (27)
MERGE INTO neighborhoods n USING (SELECT 27110 id, '대구광역시' city, '중구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 27140 id, '대구광역시' city, '동구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 27170 id, '대구광역시' city, '서구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 27200 id, '대구광역시' city, '남구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 27230 id, '대구광역시' city, '북구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 27260 id, '대구광역시' city, '수성구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 27290 id, '대구광역시' city, '달서구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 27710 id, '대구광역시' city, '달성군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 27720 id, '대구광역시' city, '군위군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);

-- 4. 인천광역시 (28)
MERGE INTO neighborhoods n USING (SELECT 28110 id, '인천광역시' city, '중구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 28140 id, '인천광역시' city, '동구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 28177 id, '인천광역시' city, '미추홀구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 28185 id, '인천광역시' city, '연수구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 28200 id, '인천광역시' city, '남동구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 28237 id, '인천광역시' city, '부평구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 28245 id, '인천광역시' city, '계양구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 28260 id, '인천광역시' city, '서구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 28710 id, '인천광역시' city, '강화군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 28720 id, '인천광역시' city, '옹진군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
-- 5. 광주광역시 (29)
MERGE INTO neighborhoods n USING (SELECT 29110 id, '광주광역시' city, '동구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 29140 id, '광주광역시' city, '서구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 29155 id, '광주광역시' city, '남구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 29170 id, '광주광역시' city, '북구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 29200 id, '광주광역시' city, '광산구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);

-- 6. 대전광역시 (30)
MERGE INTO neighborhoods n USING (SELECT 30110 id, '대전광역시' city, '동구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 30140 id, '대전광역시' city, '중구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 30170 id, '대전광역시' city, '서구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 30200 id, '대전광역시' city, '유성구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 30230 id, '대전광역시' city, '대덕구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);

-- 7. 울산광역시 (31)
MERGE INTO neighborhoods n USING (SELECT 31110 id, '울산광역시' city, '중구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 31140 id, '울산광역시' city, '남구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 31170 id, '울산광역시' city, '동구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 31200 id, '울산광역시' city, '북구' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 31710 id, '울산광역시' city, '울주군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);

-- 8. 세종특별자치시 (36)
MERGE INTO neighborhoods n USING (SELECT 36110 id, '세종특별자치시' city, '세종시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);

-- 9. 경기도 (41)
MERGE INTO neighborhoods n USING (SELECT 41110 id, '경기도' city, '수원시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41130 id, '경기도' city, '성남시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41150 id, '경기도' city, '의정부시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41170 id, '경기도' city, '안양시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41190 id, '경기도' city, '부천시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41210 id, '경기도' city, '광명시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41220 id, '경기도' city, '평택시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41250 id, '경기도' city, '동두천시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41270 id, '경기도' city, '안산시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41280 id, '경기도' city, '고양시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41290 id, '경기도' city, '과천시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41310 id, '경기도' city, '구리시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41360 id, '경기도' city, '남양주시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41370 id, '경기도' city, '오산시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41390 id, '경기도' city, '시흥시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41410 id, '경기도' city, '군포시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41430 id, '경기도' city, '의왕시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41450 id, '경기도' city, '하남시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41460 id, '경기도' city, '용인시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41480 id, '경기도' city, '파주시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41500 id, '경기도' city, '이천시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41550 id, '경기도' city, '안성시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41570 id, '경기도' city, '김포시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41590 id, '경기도' city, '화성시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41610 id, '경기도' city, '광주시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41630 id, '경기도' city, '양주시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41650 id, '경기도' city, '포천시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41670 id, '경기도' city, '여주시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41800 id, '경기도' city, '연천군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41820 id, '경기도' city, '가평군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 41830 id, '경기도' city, '양평군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);

-- 10. 강원특별자치도 (51)
MERGE INTO neighborhoods n USING (SELECT 51110 id, '강원특별자치도' city, '춘천시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 51130 id, '강원특별자치도' city, '원주시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 51150 id, '강원특별자치도' city, '강릉시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 51170 id, '강원특별자치도' city, '동해시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 51190 id, '강원특별자치도' city, '태백시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 51210 id, '강원특별자치도' city, '속초시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 51230 id, '강원특별자치도' city, '삼척시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 51720 id, '강원특별자치도' city, '홍천군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 51730 id, '강원특별자치도' city, '횡성군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 51750 id, '강원특별자치도' city, '영월군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 51760 id, '강원특별자치도' city, '평창군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 51770 id, '강원특별자치도' city, '정선군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 51780 id, '강원특별자치도' city, '철원군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 51790 id, '강원특별자치도' city, '화천군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 51800 id, '강원특별자치도' city, '양구군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 51810 id, '강원특별자치도' city, '인제군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 51820 id, '강원특별자치도' city, '고성군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 51830 id, '강원특별자치도' city, '양양군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);


-- 11. 충청북도 (43)
MERGE INTO neighborhoods n USING (SELECT 43110 id, '충청북도' city, '청주시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 43130 id, '충청북도' city, '충주시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 43150 id, '충청북도' city, '제천시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 43720 id, '충청북도' city, '보은군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 43730 id, '충청북도' city, '옥천군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 43740 id, '충청북도' city, '영동군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 43745 id, '충청북도' city, '증평군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 43750 id, '충청북도' city, '진천군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 43760 id, '충청북도' city, '괴산군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 43770 id, '충청북도' city, '음성군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 43800 id, '충청북도' city, '단양군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);

-- 12. 충청남도 (44)
MERGE INTO neighborhoods n USING (SELECT 44130 id, '충청남도' city, '천안시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 44150 id, '충청남도' city, '공주시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 44180 id, '충청남도' city, '보령시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 44200 id, '충청남도' city, '아산시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 44210 id, '충청남도' city, '서산시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 44230 id, '충청남도' city, '논산시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 44250 id, '충청남도' city, '계룡시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 44270 id, '충청남도' city, '당진시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 44710 id, '충청남도' city, '금산군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 44760 id, '충청남도' city, '부여군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 44770 id, '충청남도' city, '서천군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 44790 id, '충청남도' city, '청양군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 44800 id, '충청남도' city, '홍성군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 44810 id, '충청남도' city, '예산군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 44825 id, '충청남도' city, '태안군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);

-- 13. 전북특별자치도 (45)
MERGE INTO neighborhoods n USING (SELECT 45110 id, '전북특별자치도' city, '전주시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 45130 id, '전북특별자치도' city, '군산시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 45140 id, '전북특별자치도' city, '익산시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 45180 id, '전북특별자치도' city, '정읍시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 45190 id, '전북특별자치도' city, '남원시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 45210 id, '전북특별자치도' city, '김제시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 45710 id, '전북특별자치도' city, '완주군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 45720 id, '전북특별자치도' city, '진안군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 45730 id, '전북특별자치도' city, '무주군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 45740 id, '전북특별자치도' city, '장수군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 45750 id, '전북특별자치도' city, '임실군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 45770 id, '전북특별자치도' city, '순창군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 45790 id, '전북특별자치도' city, '고창군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 45800 id, '전북특별자치도' city, '부안군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);

-- 14. 전라남도 (46)
MERGE INTO neighborhoods n USING (SELECT 46110 id, '전라남도' city, '목포시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 46130 id, '전라남도' city, '여수시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 46150 id, '전라남도' city, '순천시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 46170 id, '전라남도' city, '나주시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 46230 id, '전라남도' city, '광양시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 46710 id, '전라남도' city, '담양군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 46720 id, '전라남도' city, '곡성군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 46730 id, '전라남도' city, '구례군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 46770 id, '전라남도' city, '고흥군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 46780 id, '전라남도' city, '보성군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 46790 id, '전라남도' city, '화순군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 46800 id, '전라남도' city, '장흥군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 46810 id, '전라남도' city, '강진군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 46820 id, '전라남도' city, '해남군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 46830 id, '전라남도' city, '영암군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 46840 id, '전라남도' city, '무안군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 46860 id, '전라남도' city, '함평군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 46870 id, '전라남도' city, '영광군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 46880 id, '전라남도' city, '장성군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 46890 id, '전라남도' city, '완도군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 46900 id, '전라남도' city, '진도군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 46910 id, '전라남도' city, '신안군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);

-- 15. 경상북도 (47)
MERGE INTO neighborhoods n USING (SELECT 47110 id, '경상북도' city, '포항시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 47130 id, '경상북도' city, '경주시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 47150 id, '경상북도' city, '김천시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 47170 id, '경상북도' city, '안동시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 47190 id, '경상북도' city, '구미시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 47210 id, '경상북도' city, '영주시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 47230 id, '경상북도' city, '영천시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 47250 id, '경상북도' city, '상주시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 47280 id, '경상북도' city, '문경시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 47290 id, '경상북도' city, '경산시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 47730 id, '경상북도' city, '의성군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 47750 id, '경상북도' city, '청송군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 47760 id, '경상북도' city, '영양군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 47770 id, '경상북도' city, '영덕군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 47820 id, '경상북도' city, '청도군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 47830 id, '경상북도' city, '고령군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 47840 id, '경상북도' city, '성주군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 47850 id, '경상북도' city, '칠곡군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 47900 id, '경상북도' city, '예천군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 47920 id, '경상북도' city, '봉화군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 47930 id, '경상북도' city, '울진군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 47940 id, '경상북도' city, '울릉군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);

-- 16. 경상남도 (48)
MERGE INTO neighborhoods n USING (SELECT 48120 id, '경상남도' city, '창원시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 48170 id, '경상남도' city, '진주시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 48220 id, '경상남도' city, '통영시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 48240 id, '경상남도' city, '사천시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 48250 id, '경상남도' city, '김해시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 48270 id, '경상남도' city, '밀양시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 48310 id, '경상남도' city, '거제시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 48330 id, '경상남도' city, '양산시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 48720 id, '경상남도' city, '의령군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 48730 id, '경상남도' city, '함안군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 48740 id, '경상남도' city, '창녕군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 48820 id, '경상남도' city, '고성군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 48840 id, '경상남도' city, '남해군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 48850 id, '경상남도' city, '하동군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 48860 id, '경상남도' city, '산청군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 48870 id, '경상남도' city, '함양군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 48880 id, '경상남도' city, '거창군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 48890 id, '경상남도' city, '합천군' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);

-- 17. 제주특별자치도 (50)
MERGE INTO neighborhoods n USING (SELECT 50110 id, '제주특별자치도' city, '제주시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);
MERGE INTO neighborhoods n USING (SELECT 50130 id, '제주특별자치도' city, '서귀포시' display FROM dual) s ON (n.neighborhood_id = s.id) WHEN NOT MATCHED THEN INSERT (neighborhood_id, city_name, display_name) VALUES (s.id, s.city, s.display);

-- FoodCategoryType Enum 구조에 맞춘 초기 카테고리 데이터 삽입
-- 중복 삽입 방지를 위해 키가 없을 때만 들어가도록 작성하는 것이 좋습니다.

MERGE INTO food_categories f
USING (
    SELECT 1 as id, '육류' as name FROM dual UNION ALL
    SELECT 2, '양념' FROM dual UNION ALL
    SELECT 3, '채소' FROM dual UNION ALL
    SELECT 4, '유제품' FROM dual UNION ALL
    SELECT 5, '해산물' FROM dual UNION ALL
    SELECT 6, '과일' FROM dual
) s ON (f.category_id = s.id)
WHEN NOT MATCHED THEN
    INSERT (category_id, name) VALUES (s.id, s.name);

-- 2. 기본 양념 및 소스 (PANTRY_ITEMS)
-- PantryItem 엔티티의 필수 컬럼(UserId, ItemName, DelFlag, EnrollDate)을 모두 포함합니다.
-- 이미 데이터가 있는 경우 중복 삽입되지 않도록 상품명(ITEM_NAME) 기준으로 MERGE 합니다.
MERGE INTO PANTRY_ITEMS p
USING (
    -- 기본 양념
    SELECT '설탕' as name FROM dual UNION ALL
    SELECT '소금' FROM dual UNION ALL
    SELECT '고춧가루' FROM dual UNION ALL
    SELECT '후추' FROM dual UNION ALL
    SELECT '미원(MSG)' FROM dual UNION ALL
    SELECT '다시다' FROM dual UNION ALL
    -- 액체 양념
    SELECT '진간장' FROM dual UNION ALL
    SELECT '국간장' FROM dual UNION ALL
    SELECT '식초' FROM dual UNION ALL
    SELECT '맛술(미림)' FROM dual UNION ALL
    SELECT '액젓' FROM dual UNION ALL
    SELECT '레몬즙' FROM dual UNION ALL
    -- 장류 및 소스
    SELECT '고추장' FROM dual UNION ALL
    SELECT '된장' FROM dual UNION ALL
    SELECT '쌈장' FROM dual UNION ALL
    SELECT '굴소스' FROM dual UNION ALL
    SELECT '치킨스톡' FROM dual UNION ALL
    SELECT '두반장' FROM dual UNION ALL
    -- 유지류
    SELECT '식용유' FROM dual UNION ALL
    SELECT '참기름' FROM dual UNION ALL
    SELECT '들기름' FROM dual UNION ALL
    SELECT '올리브유' FROM dual UNION ALL
    SELECT '버터' FROM dual UNION ALL
    -- 글로벌 소스
    SELECT '케첩' FROM dual UNION ALL
    SELECT '마요네즈' FROM dual UNION ALL
    SELECT '머스터드' FROM dual UNION ALL
    SELECT '스리라차' FROM dual UNION ALL
    SELECT '돈가스소스' FROM dual UNION ALL
    -- 향신료 및 허브
    SELECT '카레가루' FROM dual UNION ALL
    SELECT '와사비' FROM dual UNION ALL
    SELECT '파슬리' FROM dual UNION ALL
    SELECT '바질' FROM dual UNION ALL
    SELECT '월계수잎' FROM dual UNION ALL
    SELECT '시나몬가루' FROM dual
) s ON (p.ITEM_NAME = s.name AND p.USER_ID = 1)
WHEN NOT MATCHED THEN
    INSERT (PANTRY_ITEM_ID, USER_ID, ITEM_NAME, DELFLAG, ENROLLDATE)
    VALUES (PANTRY_ITEMS_SEQ.NEXTVAL, 1, s.name, 'N', CURRENT_TIMESTAMP);

-- 3. 변경 사항 확정
COMMIT;
