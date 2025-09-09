package com.coffee.test;

/*
 페이징 관련 State
    const [paging, setPaging] = useState({
        totalElements: 165, // 전체 데이터 개수
        totalPages: 17, // 전체 페이지 번호
        pageNumber: 14, // 현재 페이지 번호
        pageSize: 10, // 한 페이지에 보여 주고자 하는 데이터 개수
        beginPage: 0, // 페이징 시작 번호
        endPage: 0, // 페이징 끝 번호
        pageCount: 10 // 페이지 하단 버튼 개수
    });
위와 같을 때
 beginPage: 11
  endPage: 17
를 구해주는 공식 도출하기
*/

class Paging {
    private int totalElements = 165 ; // 전체 데이터 개수
    private int totalPages = 17 ; // 전체 페이지 번호
    private int pageNumber = 6 ; // 현재 페이지 번호
    private int pageSize = 10 ; // 한 페이지에 보여 주고자 하는 데이터 개수
    private int beginPage = 0 ;  // 페이징 시작 번호
    private int endPage = 0 ;  // 페이징 끝 번호
    private int pageCount = 10 ;  // 페이지 하단 버튼 개수

    public Paging() {
        // pageNumber가 0부터 시작시 코드
        beginPage = (pageNumber / pageCount) * pageCount + 1;

        // pageNumber가 1부터 시작시 코드
        //beginPage = ((pageNumber - 1) / pageCount) * pageCount + 1;
        endPage = Math.min(beginPage + pageCount - 1, totalPages);
        System.out.println("beginPage: " + beginPage);
        System.out.println("endPage: " + endPage);
    }
}

public class PagingTest {
    public static void main(String[] args) {
        Paging pageInfo = new Paging();
    }
}
