package com.vtcampus.lostandfound.repositories;

import com.vtcampus.lostandfound.domain.entities.ItemReport;
import com.vtcampus.lostandfound.domain.entities.ItemType;
import com.vtcampus.lostandfound.domain.entities.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ItemReportRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ItemReportRepository itemReportRepository;

    @Test
    public void testThatItemReportCanBeSaved() {
        Users user = Users.builder()
                .name("Test User")
                .email("test@example.com")
                .password("test-password")
                .build();

        Users savedUser = usersRepository.save(user);

        ItemReport report = ItemReport.builder()
                .title("Lost wallet")
                .description("A black leather wallet")
                .type(ItemType.LOST)
                .location("College library")
                .dateOccurred(LocalDate.now())
                .reportedBy(savedUser)
                .build();

        ItemReport savedReport = itemReportRepository.save(report);

        Optional<ItemReport> result =
                itemReportRepository.findById(savedReport.getId());

        assertTrue(result.isPresent());
        assertEquals(savedUser.getId(), result.get().getReportedBy().getId());
    }
}