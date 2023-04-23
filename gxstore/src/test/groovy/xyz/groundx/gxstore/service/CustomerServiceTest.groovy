package xyz.groundx.gxstore.service

import spock.lang.Specification
import xyz.groundx.gxstore.exception.CustomerNotFoundException
import xyz.groundx.gxstore.model.Customer
import xyz.groundx.gxstore.model.CustomerDto
import xyz.groundx.gxstore.repository.CustomerRepository

class CustomerServiceTest extends Specification {
    CustomerService service

    CustomerRepository customerRepository

    void setup() {
        // mock
        customerRepository = Mock()
        service = new CustomerService(customerRepository)
    }

    def "Register"() {
        given:
        def command = new CustomerDto.Register("jordan", "jung", "jordan.jung@groundx.xyz", "P@ssW0rd")
        and:
        def registered = new Customer(1L)
        1 * customerRepository.save(command.toCustomer()) >> registered

        when:
        def result = service.register(command)

        then:
        result == registered
    }

    def "GetAllCustomers"() {
        given:
        var list = [
                new Customer("conan", "bae", "conan.bae@groundx.xyz", "P@ssW0rd"),
                new Customer("david", "yang", "david.yang@groundx.xyz", "P@ssW0rd"),
                new Customer("dean", "lee", "dean.lee@groundx.xyz", "P@ssW0rd"),
        ]
        1 * customerRepository.findAll() >> list

        when:
        def results = service.getAllCustomers()

        then:
        results == [
                new CustomerDto.View(list[0]),
                new CustomerDto.View(list[1]),
                new CustomerDto.View(list[2]),
        ]
    }

    def "GetCustomer"() {
        given:
        def customerId = 3023023L
        def detail = new Customer(customerId, "jordan", "jung", "jordan.jung@groundx.xyz", "P@ssword")
        1 * customerRepository.findById(customerId) >> Optional.of(detail)

        when:
        def result = service.getCustomer(customerId)

        then:
        result == new CustomerDto.View(detail)
    }

    def "GetCustomer. but notFound > throw CustomerNotFoundException"() {
        given:
        def customerId = -10L
        1 * customerRepository.findById(customerId) >> Optional.empty()

        when:
        service.getCustomer(customerId)

        then:
        thrown(CustomerNotFoundException)

    }

    def "ModifyCustomer"() {
        given:
        def customerId = 8425188954934L
        def command = new CustomerDto.Modify("myeongju", "jeong")

        and:
        def detail = Mock(Customer)
        1 * customerRepository.findById(customerId) >> Optional.of(detail)
        1 * detail.changeName(command.firstName(), command.lastName())
        1 * customerRepository.save(detail) >> detail

        when:
        def result = service.modifyCustomer(customerId, command)

        then:
        result == new CustomerDto.View(detail)
    }

    def "ModifyCustomer. but notFound > throw CustomerNotFoundException"() {
        given:
        def customerId = -1L
        def command = new CustomerDto.Modify("myeongju", "jeong")

        and:
        1 * customerRepository.findById(customerId) >> Optional.empty()
        0 * customerRepository.save(_)

        when:
        service.modifyCustomer(customerId, command)

        then:
        thrown(CustomerNotFoundException)
    }

    def "DeleteCustomer"() {
        given:
        def customerId = 2398432L
        and:
        1 * customerRepository.deleteById(customerId)

        when:
        service.deleteCustomer(customerId)

        then:
        true
    }
}

