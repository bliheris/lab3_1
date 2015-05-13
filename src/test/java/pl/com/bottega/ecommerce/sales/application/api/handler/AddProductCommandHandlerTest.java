package pl.com.bottega.ecommerce.sales.application.api.handler;

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
import static org.mockito.Mockito.when;

public class AddProductCommandHandlerTest {

    @Test
    public void addingProductToReservation() {
        Id productId = new Id("1");
        ProductRepository productRepo = mock(ProductRepository.class);
        Product product = new Product(productId,
                new Money(new BigDecimal(19), Currency.getInstance("PLN")),
                "Product", ProductType.FOOD);
        when(productRepo.load(productId)).thenReturn(product);

        Id reservationId = new Id("1");
        Reservation reservation = new Reservation(
                reservationId, Reservation.ReservationStatus.OPENED,
                new ClientData(Id.generate(), "ClientName"), new Date());
        ReservationRepository reservationRepo = new InMemoryReservationRepo();
        reservationRepo.save(reservation);

        AddProductCommandHandler cmdHandler = new AddProductCommandHandler(
                reservationRepo, productRepo
        );

        AddProductCommand addProductCmd = new AddProductCommand(reservationId, productId , 3);

        cmdHandler.handle(addProductCmd);

        assertTrue(reservationRepo.load(reservationId).contains(product));
    }

    @Test
    public void behaviorTest() {
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