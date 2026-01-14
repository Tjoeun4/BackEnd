package com.example.project.security.token;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TokenRepository extends JpaRepository<Token, Integer> {

<<<<<<< Updated upstream
  @Query(value = """
          select t from Token t inner join Users u\s
            on t.user.id = u.id\s
            where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
  List<Token> findAllValidTokenByUser(@Param("id") Long id);
  
  Optional<Token> findByToken(String token);
}
=======
    @Query(value = """
            select t from Token t inner join t.user u
            where u.userId = :id and (t.expired = false or t.revoked = false)
            """)
    List<Token> findAllValidTokenByUser(@Param("id") Long id);
    
    Optional<Token> findByToken(String token);
}
>>>>>>> Stashed changes
