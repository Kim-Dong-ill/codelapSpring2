package com.study.spring.repository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import com.study.spring.domain.Cart;
import com.study.spring.domain.CartItem;
import com.study.spring.domain.Member;
import com.study.spring.domain.Product;
import com.study.spring.dto.CartItemListDTO;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class CartRepositoryTests {
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Test
	public void testLlistOfMember() {
		String email = "user1@aaa.com";
		List<CartItemListDTO> cartItemList = cartItemRepository.getItemsOfCartDTOByEmail(email);
		for(CartItemListDTO dto:cartItemList) {
			System.out.println(dto);
		}
	}
	
//	@Transactional
//	@Commit
//	@Test
//	public void TestInsertByProduct() {
//		String email = "user2@aaa.com";
//		Long pno = 3L;
//		int qty = 3;
//		
//		
//		//이메일을 사용해서 cartItem에 데이터 있는지 확인
//		CartItem cartItem = cartItemRepository.getItemOfPno(email, pno);
//		
//		//유저의 카트에 / 같은 제품이 있다면~
//		if(cartItem != null) {
//			cartItem.changeQty(qty);
//			cartItemRepository.save(cartItem);
//			return;
//		}
//		
//		//email로 유저의 카트가 있는지 확인했더니
//		Optional<Cart> result = cartRepository.getCartOfMember(email);
//		
//		Cart cart =null;
//		
//		//카트가 없을때~
//		if(result.isEmpty()) {
//			Member member = Member.builder().email(email).build();
//			Cart tempCart = Cart.builder().owner(member).build();
//			
//			cart = cartRepository.save(tempCart);
//		//카트가 있을때~
//		}else {
//			//카트 가져온다.
//			cart = result.get();
//		}
//		
//		//카트가 생성되고 상품에서 pno가져오고
//		Product product = Product.builder().pno(pno).build();
//		//상품이랑 유저가 입력한 개수+ 만든 카드를 아이템에 넣는다.
//		cartItem = CartItem.builder()
//				.cart(cart)
//				.qty(qty)
//				.product(product)
//				.build();
//		
//		cartItemRepository.save(cartItem);
//	}
	
	@Test
	public void testUpdateByCino() {
		Long cino =1L;
		int qty = 100;
		
		//카트 아이템에서 카트id로 있는지 확인
		Optional<CartItem> result = cartItemRepository.findById(cino);
		
		//에러처리
		CartItem cartItem = result.orElseThrow();
		
		//수량 변경
		cartItem.changeQty(qty);
		cartItemRepository.save(cartItem);
	}
	
	//삭제 데스트
	@Test
	public void testDeleteThenList() {
		Long cino = 2L;
		Long cno =cartItemRepository.getCartFromItem(cino); 
		
		cartItemRepository.deleteById(cino);
		
		List<CartItemListDTO> cartItemList = cartItemRepository.getItemsOfCartDTOByCart(cno);
		
		for(CartItemListDTO dto:cartItemList) {
			System.out.println(dto);
		}
	}
}






