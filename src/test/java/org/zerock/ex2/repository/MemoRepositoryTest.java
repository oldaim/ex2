package org.zerock.ex2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex2.entity.Memo;


import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTest {
    @Autowired
    MemoRepository memoRepository;

    public void testClass(){
        System.out.println(memoRepository.getClass().getName());

    }

    @Test
    public void testInsertDummies(){ //100개의 임의 데이터 삽입해서 테스트
        IntStream.rangeClosed(1,100).forEach(i->{
            Memo memo = Memo.builder().memoText("Sample..."+i).build();
            memoRepository.save(memo);
        });
    }

    @Test
    public void testSelect(){ // 데이터 조회 방식 findById
        Long mno =100L;
        Optional<Memo> result = memoRepository.findById(mno);

        System.out.println("==============================================");

        if(result.isPresent()){
            Memo memo =result.get();
            System.out.println(memo);
        }
    }

    @Transactional
    @Test
    public void testSelect2(){ // 데이터 조회 방식 getOne
        Long mno =100L;

        Memo memo =memoRepository.getOne(mno);

        System.out.println("=============================================");

        System.out.println(memo);
    }

    @Test
    public void testUpdate(){ //데이터 업데이트
        Memo memo = Memo.builder().mno(97L).memoText("Update Text").build();

        System.out.println(memoRepository.save(memo));
    }

    @Test
    public void testDelete(){ //데이터 삭제
        Long mno =99L;

        memoRepository.deleteById(mno);

    }

    @Test
    public void testPageDefault(){ //페이징 처리
        Pageable pageable = PageRequest.of(0,10);
        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println(result);

        System.out.println("----------------------------------");

        System.out.println("Total pages: "+result.getTotalPages());
        System.out.println("Total pages: "+result.getTotalElements());
        System.out.println("Total pages: "+result.getNumber());
        System.out.println("Total pages: "+result.getSize());
        System.out.println("Total pages: "+result.hasNext());
        System.out.println("Total pages: "+result.isFirst());

        System.out.println("----------------------------------");

        for(Memo memo :result.getContent()){
            System.out.println(memo);
        }
    }

    @Test
    public void testSort(){ //정렬
        Sort sort1=Sort.by("mno").descending();

        Pageable pageable =PageRequest.of(0,10,sort1);
        Page<Memo> result = memoRepository.findAll(pageable);
        result.get().forEach(memo -> {System.out.println(memo);}
        );
    }

    @Test
    public void testQueryMethods(){ //쿼리메소드-> JPArepository 에서 메소드 생성
        List<Memo> list =memoRepository.findByMnoBetweenOrderByMnoDesc(70L,80L);

        for(Memo memo: list){
            System.out.println(memo);
        }
    }

    @Test
    public void testQueryMethodWithPageable(){ //쿼리메소드-> JPA repository 에서 메소드 생성
        Pageable pageable = PageRequest.of(0,10,Sort.by("mno").descending());

        Page<Memo> result =memoRepository.findByMnoBetween(10L,50L,pageable);

        result.get().forEach(memo-> System.out.println(memo));

    }

    @Commit
    @Transactional
    @Test
    public void testDeleteQueryMethods(){ //쿼리메소드-> JPA repository 에서 메소드 생성
        memoRepository.deleteMemoByMnoLessThan(10L);
    }
}
