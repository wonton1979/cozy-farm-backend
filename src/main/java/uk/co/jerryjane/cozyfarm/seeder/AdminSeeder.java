package uk.co.jerryjane.cozyfarm.seeder;


import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import uk.co.jerryjane.cozyfarm.dto.AdminCredentialsRequest;
import uk.co.jerryjane.cozyfarm.service.AdminService;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final AdminService adminService;
    private final String userOneEmail;
    private final String userTwoEmail;
    private final String userOnePassword;
    private final String userTwoPassword;

    public AdminSeeder(@Value("${admin.user.one.email}") String userOneEmail,
                       @Value("${admin.user.one.password}") String userOnePassword,
                       @Value("${admin.user.two.email}") String userTwoEmail,
                       @Value("${admin.user.two.password}") String userTwoPassword,
                       AdminService adminService
    ) {
        this.userOneEmail = userOneEmail;
        this.userTwoEmail = userTwoEmail;
        this.userOnePassword = userOnePassword;
        this.userTwoPassword = userTwoPassword;
        this.adminService = adminService;
    }

    @Override
    @Transactional
    public void run(String @NonNull ... args) throws Exception {

        AdminCredentialsRequest userOne =
                new AdminCredentialsRequest(userOneEmail, userOnePassword);

        AdminCredentialsRequest userTwo =
                new AdminCredentialsRequest(userTwoEmail, userTwoPassword);

        adminService.saveAdmin(userOne);
        adminService.saveAdmin(userTwo);
        System.out.println(
                    "Admin details have been added to the database"
            );
        }
}
