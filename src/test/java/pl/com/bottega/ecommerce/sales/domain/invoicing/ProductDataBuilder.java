package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

public class ProductDataBuilder implements Builder<ProductData> {

    private Money price;
    private String name;
    private ProductType pt;

    public static ProductDataBuilder productData() {
        return new ProductDataBuilder();
    }

    public ProductDataBuilder withPlnPrice(int value){
        this.price = new Money(new BigDecimal(value), Currency.getInstance("PLN"));
        return this;
    }

    public ProductDataBuilder withName(String name){
        this.name = name;
        return this;
    }

    public ProductDataBuilder withType(ProductType pt){
        this.pt = pt;
        return this;
    }

    @Override
    public ProductData build() {
        return new ProductData(
                Id.generate(),
                price,
                name,
                pt,
                new Date()
        );
    }
}
