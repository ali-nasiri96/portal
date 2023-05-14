package com.example.portal.repository;



import com.example.portal.model.entity.SignedOutToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignedOutTokenRepository extends CrudRepository<SignedOutToken, Long> {
    SignedOutToken findByTokenId(String tokenId);
}
