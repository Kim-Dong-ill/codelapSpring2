package com.study.spring.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.study.spring.domain.Cart;
import com.study.spring.domain.CartItem;
import com.study.spring.domain.Member;
import com.study.spring.domain.Product;
import com.study.spring.dto.CartItemDTO;
import com.study.spring.dto.CartItemListDTO;
import com.study.spring.repository.CartItemRepository;
import com.study.spring.repository.CartRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class CartServiceImpl implements CartService{

	private final CartItemRepository cartItemRepository;
	private final CartRepository cartRepository;
	
	@Override
	public List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO) {
		
		String email = cartItemDTO.getEmail();
		Long pno = cartItemDTO.getPno();
		int qty = cartItemDTO.getQty();
		Long cino = cartItemDTO.getCino();
		
		if(cino != null) {
			//카트 아이템을 불러온다
			Optional<CartItem> cartItemResult = cartItemRepository.findById(cino);
			
			//없으면 에러
			CartItem cartItem = cartItemResult.orElseThrow();
			
			//수량 변경
			cartItem.changeQty(qty);
			
			//저장
			cartItemRepository.save(cartItem);
			
			//데이터 저장으 끝나고 email을 통해서 데이터를 다시 불러오게 한다.
			return getCartItems(email);
		}
		
		//이메일로 카트 가져오게한다.
		Cart cart = getCart(email);
		
		CartItem cartItem = null;
		
		//이미 동일한 상품이 담겨있을때
		cartItem = cartItemRepository.getItemOfPno(email, pno);
		
		//만약 없다면
		if(cartItem == null) {
			
			//상품 pno 넣고
			Product product = Product.builder()
					.pno(pno)
					.build();
			
			//카트 아이템 넣어준다.
			cartItem = CartItem.builder()
					.product(product)
					.cart(cart)
					.qty(qty)
					.build();
		//있다면
		}else {
			
			//수량 변경
			cartItem.changeQty(qty);
		}
		
		//상품 아이템 저장
		cartItemRepository.save(cartItem);
		
		return getCartItems(email);
	}

	private Cart getCart(String email) {
		
		Cart cart = null;
		//이메일을 통해서 카트가 있는지 없는지확인
		Optional<Cart> result = cartRepository.getCartOfMember(email);
		
		//카트가 없다면 추가해야하니까
		if(result.isEmpty()) {
			Member member = Member.builder().email(email).build();
			Cart tempCart = Cart.builder().owner(member).build();
			
			cart = cartRepository.save(tempCart);
		//카트가 있을때~
		}else {
			//카트 가져온다.
			cart = result.get();
		}
		
		return cart;
	}

	//이메일로 카트 아이템 가져오기
	@Override
	public List<CartItemListDTO> getCartItems(String email) {

		return cartItemRepository.getItemsOfCartDTOByEmail(email);
	}

	//카트 아이템 삭제하기
	@Override
	public List<CartItemListDTO> remove(Long cino) {

		//카트아이템no로 카트no 가져오기
		Long cno = cartItemRepository.getCartFromItem(cino);
		
		//삭제하기
		cartItemRepository.deleteById(cino);
		
		//다시 불러오기
		return cartItemRepository.getItemsOfCartDTOByCart(cno);
	}

}






