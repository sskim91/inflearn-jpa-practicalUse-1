package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        //엔티티를 변경할 때는 항상 변경 감지를 사용
        //컨트롤러에서 어설프게 엔티티 생성 X
        //트랜잭션이 있는 서비스 계층에서 식별자('id')와 변경할 데이터를 명확하게 전달 (파라미터 or DTO)
        //트랜잭션이 있는 서비스 계층에서 영속 상태의 엔티티를 조회하고, 엔티티의 데이터를 직접 변경
        //트랜잭션 커밋 시점에 변경 감지가 실행된다.
        Item findItem = itemRepository.findOne(itemId);
        //setter 없이 엔티티 안에서 바로 추적할 수 있는 메서드를 사용하는것이 낫다.
        //예를 들어 findItem.change(name,price,stockQuantity);
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }

    public List<Item> findItems() {
        return itemRepository.findALL();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
