package com.coffee.controller;

import com.coffee.constant.Category;
import com.coffee.dto.SearchDto;
import com.coffee.entity.Product;
import com.coffee.repository.ProductRepository;
import com.coffee.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Map;

// 상품 정보 요청을 처리해주는 Controller
// @RequestMapping에 여러 개의 요청을 처리하려면 중괄호 기호를 사용하면 됩니다.
@RestController
@RequestMapping({"/product", "/products"})
public class ProductController {
    private ProductService productService ;

    @Autowired
    private ProductRepository productRepository ;

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Product updateProduct){
        // updateProduct : 관리자가 수정하고자 하는 상품 정보
        return productRepository.findById(id) // Optional<Product> 반환
                .map(product -> { // Optional에 값이 존재하면 실행
                    try{ // 상품 업데이트 로직 수행
                        // product는 실제 데이터 베이스에 저장할 상품 정보
                        product.setName(updateProduct.getName());
                        product.setPrice(updateProduct.getPrice());
                        product.setCategory(updateProduct.getCategory());
                        product.setStock(updateProduct.getStock());
                        product.setDescription(updateProduct.getDescription());

                        // 이미지 업데이트 로직(Base64)
                        if(updateProduct.getImage() != null && updateProduct.getImage().startsWith("data:image")){
                            String imageFileName = saveProductImage(updateProduct.getImage());
                            product.setImage(imageFileName);
                        }

                        productRepository.save(product); // 해당 상품을 데이터 베이스에 수정
                        return ResponseEntity.ok(Map.of("message", "상품 수정 성공")) ;

                    }catch (Exception ex){
                        return ResponseEntity.status(500).body(Map.of("message", ex.getMessage(), "error", "error updating file"));
                    }
                })
                .orElse(ResponseEntity.notFound().build()) ; // 값이 존재하지 않으면 404 NotFound 반환
    }

    // 이미지 저장 메소드
    private String saveProductImage(String base64Image) throws Exception {
        byte[] decodedImage = Base64.getDecoder().decode(base64Image.split(",")[1]);

        // 저장될 물리적 파일 이름(데이터 베이스에 저장될 이름)
        String imageFileName = "product_" + System.currentTimeMillis() + ".jpg";

        // File : 파일이나 폴더를 처리하기 위한 자바 클래스
//        String pathName = "c:/boot/images/" ;
        String pathName = "/home/ubuntu/shop/image/" ;
        File imageFile = new File(pathName + imageFileName);

        // FileOutputStream : 바이트 파일을 처리해주는 자바 스트림 클래스
        try( FileOutputStream fos = new FileOutputStream(imageFile)){
            fos.write(decodedImage); // 해당 경로에 파일에 쓰기(즉, 업로드)
        }

        return imageFileName ;
    }


    public ProductController(ProductService productService) {
        this.productService = productService; // 생성자 주입
    }

//    // http://localhost:9000/product/list
//    @GetMapping("/list") // 상품 목록 보여 주세요.
//    public ResponseEntity<List<Product>> list(){
//        List<Product> products = this.productService.listProducts() ;
//        System.out.println("상품 개수 : " + products.size());
//
//        // Http 응답 코드 200(성공)과 함께 상품 정보를 json으로 반환해줍니다.
//        return ResponseEntity.ok(products) ;
//    }

//    // http://localhost:9000/product/list?pageNumber=페이지번호&pageSize=페이지사이즈
//    @GetMapping("/list") // 페이징 처리를 사용하여 상품 목록 일부를 보여 주세요.
//    public ResponseEntity<Page<Product>> list(
//            @RequestParam(defaultValue = "0") int pageNumber,
//            @RequestParam(defaultValue = "6")  int pageSize
//    ){
//        // pageSize개씩 묶은 다음에 pageNumber 페이지의 내용을 추출합니다.(0 base)
//        Pageable pageable = PageRequest.of(pageNumber, pageSize);
//
//        Page<Product> products = this.productService.listProducts(pageable) ;
//
//        System.out.println("총 상품 개수 : " + products.getTotalElements());
//        System.out.println("총 페이지 번호 : " + products.getTotalPages());
//        System.out.println("현재 페이지 번호 : " + products.getNumber());
//
//        // Http 응답 코드 200(성공)과 함께 상품 정보를 json으로 반환해줍니다.
//        return ResponseEntity.ok(products) ;
//    }

    // http://localhost:9000/product/list?pageNumber=페이지번호&pageSize=페이지사이즈
    @GetMapping("/list") // 페이징 처리를 사용하여 상품 목록 일부를 보여 주세요.
    public ResponseEntity<Page<Product>> list(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "6")  int pageSize,
            @RequestParam(defaultValue = "all") String searchDateType,
            @RequestParam(defaultValue = "") Category category,
            @RequestParam(defaultValue = "") String searchMode,
            @RequestParam(defaultValue = "") String searchKeyword
    ){
        // searchDto 객체로 파라미터 값 설정
        SearchDto searchDto = new SearchDto(searchDateType, category, searchMode, searchKeyword);

        // 검색 조건을 적용하여 상품 목록 가져오기
        Page<Product> products = this.productService.listProducts(searchDto, pageNumber, pageSize);

        System.out.println("검색 조건: " + searchDto);
        System.out.println("총 상품 개수 : " + products.getTotalElements());
        System.out.println("총 페이지 번호 : " + products.getTotalPages());
        System.out.println("현재 페이지 번호 : " + products.getNumber());

        // Http 응답 코드 200(성공)과 함께 상품 정보를 json으로 반환해줍니다.
        return ResponseEntity.ok(products) ;
    }

    // @PathVariable : url 경로 내의 변수 값을 파라미터로 매핑할 때 사용합니다.
    // http://localhost:9000/product/detail/상품번호
    @GetMapping("/detail/{id}")
    public ResponseEntity<Product> detail(@PathVariable Long id){
        Product product = this.productService.getProductById(id);

        if(product != null){
            return ResponseEntity.ok(product); // 200 Ok 응답
        }else{ // 404 응답
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build() ;
        }
    }

    @GetMapping("/update/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id){
        System.out.println("수정할 상품 번호 : " + id);
        Product product = this.productService.getProductById(id);

        if(product != null){ // 상품이 존재하면 해당 상품 반화
            return ResponseEntity.ok(product); // 200 Ok 응답

        }else{ // 없으면 404 응답과 함께 널 값을 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) ;
        }
    }

    @PostMapping("/insert") // 상품 등록
    public ResponseEntity<?> insert(@RequestBody Product product){
        String imageData = product.getImage() ; // Base64 인코딩된 문자열
        byte[] decodedImage = Base64.getDecoder().decode(imageData.split(",")[1]);

        // 데이터 베이스에 저장될 문자열 형식의 이미지 이름
        String imageFileName = "product_" + System.currentTimeMillis() + ".jpg" ;

        //        String pathName = "c:/boot/images/" ;
        String pathName = "/home/ubuntu/shop/image/" ;
        File imageFile = new File(pathName + imageFileName);

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            fos.write(decodedImage); // 바이트 파일을 해당 경로에 복사하기

            product.setImage(imageFileName);
            product.setInputdate(LocalDate.now());

            productService.save(product) ;

            return ResponseEntity.ok(Map.of("message", "Product inserted successfully", "image", imageFileName));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", e.getMessage(), "error", "error uploading file"));
        }
    }
}
