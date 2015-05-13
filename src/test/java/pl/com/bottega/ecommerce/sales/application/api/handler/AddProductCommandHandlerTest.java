package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.invoicing.Tax;
import pl.com.bottega.ecommerce.sales.domain.invoicing.TaxPolicy;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddProductCommandHandlerTest {

    private ProductRepository productRepo;
    private Id productId = new Id("1");
    private Product product;

    private Id reservationId = new Id("1");
    private Reservation reservation;
    private ReservationRepository reservationRepo;

    @Before
    public void setUp(){
        product = new Product(productId,
                new Money(new BigDecimal(19), Currency.getInstance("PLN")),
                "Product", ProductType.FOOD);
        productRepo = mock(ProductRepository.class);
        when(productRepo.load(productId)).thenReturn(product);

        reservation = new Reservation(
                reservationId, Reservation.ReservationStatus.OPENED,
                new ClientData(Id.generate(), "ClientName"), new Date());
        reservationRepo = new InMemoryReservationRepo();
        reservationRepo.save(reservation);
    }

    @Test
    public void addingProductToReservation() {
        //given
        AddProductCommand addProductCmd = new AddProductCommand(reservationId, productId , 3);
        AddProductCommandHandler cmdHandler = new AddProductCommandHandler(
                reservationRepo, productRepo
        );

        //when
        cmdHandler.handle(addProductCmd);

        //then
        assertTrue(reservationRepo.load(reservationId).contains(product));
    }

    @Test
    public void properProductLoading() {
        //given
        AddProductCommand addProductCmd = new AddProductCommand(reservationId, productId , 10);
        AddProductCommandHandler cmdHandler = new AddProductCommandHandler(
                reservationRepo, productRepo
        );

        //when
        cmdHandler.handle(addProductCmd);

        //then
        verify(productRepo).load(productId);
    }

    private class InMemoryReservationRepo implements ReservationRepository {

        private Map<Id, Reservation> map = new HashMap<>();

        @Override
        public void save(Reservation reservation) {
            map.put(reservation.getId(), reservation);
        }

        @Override
        public Reservation load(Id reservationId) {
            return map.get(reservationId);
        }
    }
}