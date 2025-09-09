package com.coffee.controller;

import com.coffee.dto.CartProductRequestDto;
import com.coffee.dto.CartProductResponseDto;
import com.coffee.dto.OrderItemDto;
import com.coffee.dto.OrderRequestDto;
import com.coffee.entity.*;
import com.coffee.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final MemberRepository memberRepository ;
    private final ProductRepository productRepository ;
    private final CartRepository cartRepository ;
    private final CartProductRepository cartProductRepository ;

    private final OrderRepository orderRepository ;

    public CartController(MemberRepository memberRepository, ProductRepository productRepository, CartRepository cartRepository, CartProductRepository cartProductRepository, OrderRepository orderRepository) {
        // 생성자를 이용한 변수들의 값을 주입(Injection)
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartProductRepository = cartProductRepository;
        this.orderRepository = orderRepository;
    }
/*
Optional 클래스 관련 메소드
isEmpty() : 값이 없는 경우 true를 반환합니다.
get() : 해당 객체 정보를 가져 옵니다.
orElseGet() : 값이 존재하면 해당 값을 반환, 없으면 () 내의 결과 값을 반환.
orElse() : 객체가 존재하지 않는 경우, 반환할 기본 값을 정의합니다.
*/

    @GetMapping("/list/{memberId}") // http://localhost:9000/cart/list/회원아이디
    public ResponseEntity<List<CartProductResponseDto>> getCartProducts(@PathVariable Long memberId){
        System.out.println("회원 아이디 : " + memberId);

        Optional<Member> memberOptional = memberRepository.findById(memberId);
        if(memberOptional.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        Member member = memberOptional.get() ;
        Cart cart = cartRepository.findByMember(member).orElse(new Cart());

        // 과거 내가 장바구니(카트)에 담아 두었던 목록을 의미하는 List 컬렉션
        List<CartProductResponseDto> cartProducts = new ArrayList<CartProductResponseDto>();

        for(CartProduct cp  : cart.getCartProducts()){
            cartProducts.add(new CartProductResponseDto(cp));
        }

        System.out.println("카트 상품 개수 : " + cartProducts.size());

        return ResponseEntity.ok(cartProducts) ; // 전체 카트 상품 반환
    }



    @PostMapping("/insert")
    public ResponseEntity<String> addToCart(@RequestBody CartProductRequestDto dto){
        Optional<Member> memberOptional = memberRepository.findById(dto.getMemberId());
        Optional<Product> productOptional = productRepository.findById(dto.getProductId()) ;

        if(memberOptional.isEmpty() || productOptional.isEmpty()){
            return ResponseEntity.badRequest().body("회원 또는 상품이 존재하지 않습니다.");
        }

        Member member = memberOptional.get();
        Product product = productOptional.get();

        // 재고 확인
        if(product.getStock() < dto.getQuantity()){
            return ResponseEntity.badRequest().body("재고 수량이 부족합니다.");
        }

        // 카트 조회 또는 신규 생성
        // 자바의 람다식
        Cart cart = cartRepository.findByMember(member)
                .orElseGet(() -> {
                    Cart newCart = new Cart(); // 신규 카트 준비
                    newCart.setMember(member); // 고객이 카트를 집어 듬
                    return cartRepository.save(newCart) ; // 데이터 베이스에 저장
                });

        // 카트에 상품 추가
        CartProduct cartProduct = new CartProduct();
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);
        cartProduct.setQuantity(dto.getQuantity());
        cartProductRepository.save(cartProduct);

//        // 재고 수량 차감
//        product.setStock(product.getStock() - dto.getQuantity());
//        productRepository.save(product);

        return ResponseEntity.ok("상품이 장바구니에 추가되었습니다.");
    }

    // http://localhost:9000/cart/edit/카트상품아이디?quantity=변경수량
    // const url = `http://localhost:9000/cart/edit/${cartProductId}?quantity=${quantity}`
    @PatchMapping("/edit/{cartProductId}")
    public ResponseEntity<String> updateCartProduct(@PathVariable Long cartProductId, @RequestParam(required = false) Integer quantity){
        System.out.println("카트 상품 아이디 : " + cartProductId);

        System.out.println("quantity : " + quantity);
        if(quantity == null){
            return ResponseEntity.badRequest().body("장바구니 품목은 최소 1개 이상이어야 합니다.");
        }

        Optional<CartProduct> cartProductOptional = cartProductRepository.findById(cartProductId);
        if(cartProductOptional.isEmpty()){
            return ResponseEntity.badRequest().body("장바구니 품목을 찾을 수 없습니다.");
        }


        CartProduct cartProduct = cartProductOptional.get() ;
        System.out.println("변경하고자 하는 수량 : " + quantity);
        cartProduct.setQuantity(quantity);  // 엔터티 수량 변경
        cartProductRepository.save(cartProduct) ; // 데이터 베이스에 저장

        String message = "카트 상품 Id [" + cartProductId + "]가 `" + quantity + "`개로 수정되었습니다.";
        return ResponseEntity.ok(message) ;
    }

    @DeleteMapping("/delete/{cartProductId}")
    public ResponseEntity<String> deleteCartProduct(@PathVariable Long cartProductId){
        System.out.println("삭제될 카트 상품 아이디 : " + cartProductId);
        cartProductRepository.deleteById(cartProductId);
        String message = "카트 상품 " + cartProductId + "번이 장바구니 목록에서 삭제되었습니다." ;
        return ResponseEntity.ok(message) ;
    }

/* 모든 카트와 주문 데이터를 지우세요.
카트 상품 4개를 장바구니에 담아 주세요.
카트 상품 4개 중 2개를 주문해 보세요.
상품 테이블의 재고 수량을 확인해 보세요.
카트 테이블을 확인해 보세요.
주문 테이블을 확인해 보세요.
상품 테이블의 재고 수량을 확인해 보세요.
*/
    @PostMapping("/order")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequestDto dto){
        System.out.println(dto);

        // 회원 객체 생성
        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

        // 주문 객체 생성
        Order order = new Order() ;
        order.setMember(member);
        order.setOrderDate(LocalDate.now());
        order.setStatus(dto.getStatus());

        // 주문 상품 객체 생성
        List<OrderProduct> orderProducts = new ArrayList<OrderProduct>();

        // 리액트에서 체크된 모든 카트 상품을 반복해서 처리합니다.
        for (OrderItemDto bean : dto.getOrderItems()){
            Long productId = bean.getProductId();
            Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

            OrderProduct op = new OrderProduct();
            op.setOrder(order);
            op.setProduct(product);
            op.setQuantity(bean.getQuantity());
            orderProducts.add(op);

            if(product.getStock() < bean.getQuantity()){
                new RuntimeException("재고 수량이 부족합니다.");
            }

            // 재고 차감
            product.setStock(product.getStock() - bean.getQuantity());

            // 카트에 담겨 있던 품목을 삭제해 줘야 합니다.
            Long cartProductId = bean.getCartProductId() ;
            cartProductRepository.deleteById(cartProductId);
        }

        // 하나의 주문에 여러 개의 상품이 저장됩니다.
        order.setOrderProducts(orderProducts);

        // 주문 정보를 데이터 베이스에 저장합니다.
        orderRepository.save(order) ;

        String message = "고객님의 주문이 완료되었습니다." ;
        return ResponseEntity.ok(message);
    }
}
