package com.example.project.domain.recipe.domain;

import java.util.ArrayList;
import java.util.List;

import com.example.project.member.domain.Users;

//JPA 및 Validation
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
//Lombok (코드 간소화)
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "user_details")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDetail {
    @Id
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private Users user;

    private String cookingLevel; // 초보, 중수, 고수
    private Integer spicyLevel;  // 1~5
    private Integer saltyLevel;  // 1~5

    @ElementCollection
    @CollectionTable(name = "user_allergies", joinColumns = @JoinColumn(name = "user_id"))
    private List<String> allergies = new ArrayList<>(); // 알러지 재료
}