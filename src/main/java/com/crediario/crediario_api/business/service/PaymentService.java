package com.crediario.crediario_api.business.service;

import com.crediario.crediario_api.business.config.SystemConfig;
import com.crediario.crediario_api.business.dto.payment.request.CreatePaymentRequest;
import com.crediario.crediario_api.business.dto.payment.response.PaymentResponse;
import com.crediario.crediario_api.business.entity.Customer;
import com.crediario.crediario_api.business.entity.Installment;
import com.crediario.crediario_api.business.entity.Payment;
import com.crediario.crediario_api.business.entity.Purchase;
import com.crediario.crediario_api.business.enums.InstallmentStatus;
import com.crediario.crediario_api.business.exception.BusinessException;
import com.crediario.crediario_api.business.mapper.PaymentMapper;
import com.crediario.crediario_api.infrastructure.repository.InstallmentRepository;
import com.crediario.crediario_api.infrastructure.repository.PaymentRepository;
import com.crediario.crediario_api.infrastructure.repository.PurchaseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InstallmentRepository installmentRepository;
    private final CustomerService customerService;
    private final PurchaseRepository purchaseRepository;
    private final SystemConfig systemConfig;

    public PaymentService(PaymentRepository paymentRepository, InstallmentRepository installmentRepository, CustomerService customerService, PurchaseRepository purchaseRepository, SystemConfig systemConfig) {
        this.paymentRepository = paymentRepository;
        this.installmentRepository = installmentRepository;
        this.customerService = customerService;
        this.purchaseRepository = purchaseRepository;
        this.systemConfig = systemConfig;
    }

    public PaymentResponse createPayment(CreatePaymentRequest request){
        Installment installment = installmentRepository.findById(request.installmentId())
                .orElseThrow(()-> new BusinessException("Installment not found."));

        if (installment.getStatus() == InstallmentStatus.PAID) {
            throw new BusinessException("Installment already paid.");
        }

        long daysLate = ChronoUnit.DAYS.between(installment.getDueDate(),LocalDate.now());

        BigDecimal fineAmount = daysLate > 0 ? installment.getValue().multiply(systemConfig.getFineRate())
                : BigDecimal.ZERO;

        BigDecimal interest = daysLate > 0 ? installment.getValue().multiply(systemConfig.getInterestRatePerDay().multiply(new BigDecimal(daysLate)))
                : BigDecimal.ZERO;

        BigDecimal totalPaid = installment.getValue().add(fineAmount.add(interest));

        Payment payment = new Payment(
                installment,
                installment.getValue(),
                fineAmount,
                interest,
                totalPaid,
                request.paymentMethod()
        );
        paymentRepository.save(payment);
        installment.setStatus(InstallmentStatus.PAID);
        installmentRepository.save(installment);

        return PaymentMapper.toResponse(payment);
    }

    public List<PaymentResponse> findByCustomer(String cpf){
        Customer customer = customerService.findByEntityByCpf(cpf);

        return paymentRepository.findByInstallmentPurchaseCustomer(customer)
                .stream()
                .map(PaymentMapper::toResponse)
                .toList();
    }
    public List<PaymentResponse> findByPurchase(Long purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new BusinessException("Purchase not found."));
        return paymentRepository.findByInstallmentPurchase(purchase)
                .stream()
                .map(PaymentMapper::toResponse)
                .toList();
    }

    public List<PaymentResponse> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return paymentRepository.findByDateBetween(start, end)
                .stream()
                .map(PaymentMapper::toResponse)
                .toList();
    }

    public List<PaymentResponse> findAll(){
        return paymentRepository.findAll()
                .stream()
                .map(PaymentMapper::toResponse)
                .toList();
    }

}
