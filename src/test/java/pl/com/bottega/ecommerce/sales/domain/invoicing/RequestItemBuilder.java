package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

public class RequestItemBuilder implements Builder<RequestItem> {

    private ProductData productData;
    private int quantity;
    private Money price;

    public static RequestItemBuilder requestItem() {
        return new RequestItemBuilder();
    }

    public RequestItemBuilder withQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public RequestItemBuilder withPlnPrice(int value){
        this.price = new Money(new BigDecimal(value), Currency.getInstance("PLN"));
        return this;
    }

    public RequestItemBuilder withProductData(ProductData productData) {
        this.productData = productData;
        return this;
    }

    @Override
    public RequestItem build() {
        return new RequestItem(productData, quantity, price);
    }
}

