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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.com.bottega.ecommerce.sales.domain.invoicing.ProductDataBuilder.productData;
import static pl.com.bottega.ecommerce.sales.domain.invoicing.RequestItemBuilder.requestItem;

public class BookKeeperTest {

    @Test
    public void issueInvoiceWithOneItem() {
        //given
        InvoiceRequest ir = new InvoiceRequest(clientData);
        ir.add(requestItem1());
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

    @Test
    public void issueInvoiceWithTwoItemsShouldCalculateTaxTwoTimes(){
        //given
        InvoiceRequest ir = new InvoiceRequest(clientData);
        ir.add(requestItem1());
        ir.add(requestItem2());

        InvoiceFactory invoiceFactory = new InvoiceFactory();
        TaxPolicy taxPolicy = mock(TaxPolicy.class);
        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class)))
                .thenReturn(new Tax(pln(2), "No reason"));
        BookKeeper bk = new BookKeeper(invoiceFactory);

        //when
        Invoice invoice = bk.issuance(ir, taxPolicy);

        //then
        assertThat(invoice.getItems().size(), is(2));
        verify(taxPolicy).calculateTax(ProductType.FOOD, pln(10));
        verify(taxPolicy).calculateTax(ProductType.DRUG, pln(15));
    }

    @Test
    public void invoiceNetAndGrossTest(){
        //given
        InvoiceRequest ir = new InvoiceRequest(clientData);
        ir.add(requestItem1());
        ir.add(requestItem2());

        InvoiceFactory invoiceFactory = new InvoiceFactory();
        TaxPolicy taxPolicy = mock(TaxPolicy.class);
        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class)))
                .thenReturn(new Tax(pln(2), "No reason"));
        BookKeeper bk = new BookKeeper(invoiceFactory);

        //when
        Invoice invoice = bk.issuance(ir, taxPolicy);

        //then
        assertThat(invoice.getNet(), is(pln(25)));
        assertThat(invoice.getGros(), is(pln(29)));
    }

    private ClientData clientData =
            new ClientData(Id.generate(), "ClientName");

    private RequestItem requestItem1() {
        return requestItem().withProductData(
                productData().
                        withName("Produkt").
                        withType(ProductType.FOOD).
                        withPlnPrice(10).build()).
                withQuantity(1).
                withPlnPrice(10).build();
    }

    private RequestItem requestItem2(){
        return requestItem().withProductData(
                productData().
                        withName("Produkt2").
                        withType(ProductType.DRUG).
                        withPlnPrice(5).build()).
                withQuantity(3).
                withPlnPrice(15).build();
    }

    private Money pln(int value){
        return new Money(new BigDecimal(value), Currency.getInstance("PLN"));
    }
}