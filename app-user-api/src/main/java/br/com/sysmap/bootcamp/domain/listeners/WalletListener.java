package br.com.sysmap.bootcamp.domain.listeners;

import br.com.sysmap.bootcamp.domain.service.WalletService;
import br.com.sysmap.bootcamp.dto.WalletMessagetDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@RabbitListener(queues = "WalletQueue")
public class WalletListener {

    @Autowired
    private  WalletService walletService;

    @RabbitHandler
    public void receive(WalletMessagetDto walleMessagetDto) {
        log.info("debiting wallet...\n {}", walletService.debit(walleMessagetDto));
    }

}