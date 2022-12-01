package com.example.ex2.repository;

import com.example.ex2.entity.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {

    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass(){
        System.out.println(memoRepository.getClass().getName());
    }
    //insert작업
    @Test
    public void testInsertDummies(){
        IntStream.rangeClosed(1,100).forEach(i -> { //100개의 메모 객체 생성
            Memo memo = Memo.builder().memoText("Sample..." +i).build();
            memoRepository.save(memo);  //엔티티 객체 save로 첫 생성이니까 insert
        });
    }
    //select작업-findById() 방식
    @Test
    public void testSelect(){
        //db에 존재하는 mno
        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);
        System.out.println("==========================");
        if(result.isPresent()){
            Memo memo = result.get();
            System.out.println(memo);
        }
    }
    //select작업-getOne() 방식
    @Transactional
    @Test
    public void testSelect2(){
        //db에 존재하는 mno
        Long mno = 100L;

        Memo memo = memoRepository.getOne(mno);
        System.out.println("==========================");
        System.out.println(memo);
    }
    //update작업
    @Test
    public void testUpdate(){
            Memo memo = Memo.builder().mno(100L).memoText("Update Text").build();
            System.out.println(memoRepository.save(memo));

    }
    //delete작업
    @Test
    public void testDelete(){
            Long mno = 100L;
            memoRepository.deleteById(mno);

    }
    //페이징 처리
    @Test
    public void testPageDefault(){
        //1페이지 10개
        Pageable pageable = PageRequest.of(0,10);//반드시 0부터시작, 1페이의 데이터 10개
        Page<Memo> result = memoRepository.findAll(pageable);   //Page<엔티티 타입>
        System.out.println(result);
        System.out.println("==========================");
        System.out.println("Total Pages: "+result.getTotalPages()); //총 몇 페이지
        System.out.println("Total Count: "+result.getTotalElements()); //전체 개수
        System.out.println("Page Number: "+result.getNumber()); //현재 페이지 번호 0부터 시작
        System.out.println("Page Size: "+result.getSize()); //페이지당 데이터 개수
        System.out.println("has next page?: "+result.hasNext()); //다음 페이지 존재 여부
        System.out.println("first page?: "+result.isFirst()); //시작 페이지(0) 여부
        System.out.println("==========================");
        for (Memo memo : result.getContent()){
            System.out.println(memo);
        }
    }
    @Test
    public void testSort(){
        Sort sort1 = Sort.by("mno").descending();   //mno 역순 정렬
        Sort sort2 = Sort.by("memoText").ascending();   //memoText 순차 정렬
        Sort sortAll = sort1.and(sort2);
        Pageable pageable = PageRequest.of(0, 10, sortAll)    //1페이의 데이터 10개, sortAll 방식 정렬
        Page<Memo> result = memoRepository.findAll(pageable);   //Page<엔티티 타입>
        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }
    //쿼리 메서드를 이용해 ~에서 ~사이 객체구하기
    @Test
    public void testQueryMethods(){
        List<Memo> List = memoRepository.findByMnoBetweenOrderByMnoDesc(70L, 80L);
        for (Memo memo : List){
            System.out.println(memo);
        }
    }
    //쿼리 메서드와 Pageable의 결합
    @Test
    public void testQueryMethodWithPageable(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
        Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageable);
        result.get().forEach(memo -> System.out.println(memo));
    }
    //아래 코드는 @Commit와 @Transactional를 하지 않으면 에러가 나고 deleteBy는 엔티티 객체를 하나씩 처리함
    //따라서 효율적인 이용을 위해서는 @Query를 이용해야함
    @Commit
    @Transactional
    @Test
    public void testDeleteQueryMethods(){
        memoRepository.deleteMemoByMnoLessThan(10L)
    }
}
