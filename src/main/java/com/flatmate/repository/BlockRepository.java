package com.flatmate.repository;

import com.flatmate.model.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlockRepository extends JpaRepository<Block, Long> {

    @Query("select count(b) > 0 from Block b where (b.blockerId = :a and b.blockedId = :b) "
            + "or (b.blockerId = :b and b.blockedId = :a)")
    boolean existsBetween(@Param("a") Long a, @Param("b") Long b);

    @Query("select b from Block b where b.blockerId = :me or b.blockedId = :me")
    List<Block> involving(@Param("me") Long me);
}