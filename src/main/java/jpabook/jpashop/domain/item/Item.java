package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //===비즈니스 로직===//
    /**
     * 엔티티 자체가 해결 할 수 있는 것은 엔티티 안에 로직을 넣는 것이 맞다.
     * 데이터를 가지고 있는 곳에서 비즈니스 로직이 나가는게 가장 응집도가 있다.
     * stockQuantity 를 바꾸려면 핵심 비즈니스 메서드를 통해서 바꾸는 것이 객체지향적이다.
     * 관리하기 용이하다는 장점도 있다.
     */
    /**
     * stock 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
