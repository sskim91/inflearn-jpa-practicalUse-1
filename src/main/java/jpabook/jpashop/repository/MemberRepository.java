package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by sskim on 2021/08/17
 * Github : http://github.com/sskim91
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

    //select m from Member m where m.name = ?
    List<Member> findByName(String name);
}
