package com.example.ex2.entity;

import lombok.*;

import javax.persistence.*;

@Entity //엔티티 클래스
@Table(name = "tbl_memo")   //테이블 이름이 tbl_memo인 테이블 생성
@ToString
@Getter //Getter메서드 생성
@Builder    //객체를 생성할 수 있게 처리

//이 두개를 항상 같이 처리해야 컴파일 에러 방지
@AllArgsConstructor
@NoArgsConstructor

public class Memo {
    @Id //엔티티 클래스는 PK에 해당하는 특정 필드를 @Id로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //PK를 자동으로 생성
    //AUTO: Hibernate가 생성 방식 결정, IDENTITY: 사용하는 DB가 키 생성을 결정
    //SEQUENCE: DB의 시퀀스를 이용해서 키 생성 @SeqenceGenerator와 같이 사용
    // TABLE: 키 생성 전용 테이블을 생성해서 키 생성 @TableGenerator와 함께 사용
    private Long mno;

    @Column(length = 200, nullable = false) //추가적인 필드(컬럼)가 필요한 경우 다양한 속성 지정
    private String memoText;
}
