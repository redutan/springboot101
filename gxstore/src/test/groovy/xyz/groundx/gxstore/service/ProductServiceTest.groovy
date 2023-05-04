package xyz.groundx.gxstore.service

import spock.lang.Specification
import xyz.groundx.gxstore.model.Product
import xyz.groundx.gxstore.repository.ProductRepository

class ProductServiceTest extends Specification {
    // SUT
    ProductService service
    // DOCs
    ProductRepository productRepository

    void setup() {
        // Mock
        productRepository = Mock()

        service = new ProductService(productRepository)
    }

    def "GetAllProducts"() {
        given:
        var products = [new Product(), new Product()]

        when:
        var results = service.getAllProducts()

        then:
        1 * productRepository.findAll() >> products
        and:
        results == products
    }

    def "GetAllProductsOnCache"() {
        given:
        var products = [new Product(), new Product()]

        when:
        var results = service.getAllProducts()
        var results2 = service.getAllProducts()

        then:
        1 * productRepository.findAll() >> products
        and:
        results == products
        results2 == products
    }

    def "GetPromotions"() {
        given:
        var products = [new Product(), new Product()]

        when:
        var results = service.getPromotions()

        then:
        1 * productRepository.findAllByPromotionIsNotNull() >> products
        and:
        results == products
    }

    def "GetPromotionsOnCache"() {
        given:
        var products = [new Product(), new Product()]

        when:
        var results = service.getPromotions()
        var results2 = service.getPromotions()

        then:
        1 * productRepository.findAllByPromotionIsNotNull() >> products
        and:
        results == products
        results2 == products
    }
}
