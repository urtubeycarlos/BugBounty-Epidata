
package edu.epidata.todo.users;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import edu.epidata.todo.log.LogService;
import edu.epidata.todo.users.CurrentUserService;
import edu.epidata.todo.users.User;

@RunWith(MockitoJUnitRunner.class)
public class CurrentUserTest {

	CurrentUserService currentUser;

	@Before
	public void setup(){
		this.currentUser = new CurrentUserService();
	}

	/**
	 * El servicio debería retornar un usuario por defecto.
	 */
	//Se agrego el current == null para que retorne el usuario por defecto
	@Test
	public void testGetsDefaultUser() {
		User user = currentUser.getCurrent();
		assertNotNull(user);
	}

	/**
	 * Chequea que si 2 threads acceden de forma concurrente obtentan el mismo usuario.
	 *
	 * @throws InterruptedException
	 */
	//Se agrego synchronized al metodo getCurrent.
	@Test
	public void testCurrentConcurrent() throws InterruptedException {
		for (int i = 0; i < 10000; i++) {
			// En este caso creamos una instancia separada para crear uno nuevo
			// en cada iteraciÃ³n
			CurrentUserService testSvc = new CurrentUserService();

			final User[] users = new User[2];

			Thread t1 = new Thread(() -> {
				users[0] = testSvc.getCurrent();
			});
			Thread t2 = new Thread(() -> {
				users[1] = testSvc.getCurrent();
			});

			t1.start();
			t2.start();

			t1.join();
			t2.join();
			assertNotNull(users[0]);
			assertNotNull(users[1]);
			assertTrue(users[0]==users[1]);
		}
	}
}
