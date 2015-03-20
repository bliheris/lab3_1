package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Assert;
import org.junit.Test;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.awt.print.Book;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookKeeperTest {

    @Test
    public void issueInvoiceWithOneItem(){
        //given
        InvoiceRequest ir = invoiceRequestWithOneItem();
        InvoiceFactory invoiceFactory = new InvoiceFactory();
        TaxPolicy taxPolicy = mock(TaxPolicy.class);
        when(taxPolicy.calculateTax(ProductType.FOOD, pln(10))).thenReturn(
                new Tax(pln(2), "No reason"));
        BookKeeper bk = new BookKeeper(invoiceFactory);

        //when
        Invoice invoice = bk.issuance(ir, taxPolicy);

        //then
        assertThat(invoice.getItems().size(), is(1));
    }

    private ClientData clientData =
            new ClientData(Id.generate(), "ClientName");

    private InvoiceRequest invoiceRequestWithOneItem() {
        InvoiceRequest ir = new InvoiceRequest(clientData);
        ProductData productData = new ProductData(
                Id.generate(),
                pln(10),
                "Produkt",
                ProductType.FOOD,
                new Date()
        );
        ir.add(new RequestItem(
                productData, 1,
        pln(10)));

        return ir;
    }

    private Money pln(int value){
        return new Money(new BigDecimal(value), Currency.getInstance("PLN"));
    }
}