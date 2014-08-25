package com.nextcontrols.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.nextcontrols.dao.KeyholderDAO;
import com.nextcontrols.domain.Keyholder;
import com.nextcontrols.domain.KeyholderList;
import com.nextcontrols.domain.KeyholderListEnablingDetails;

public class KeyholderDAOTest {

	@Test
	public void testGetInstance() {
		Assert.assertNotNull("KeyholderDAO object is null",
				KeyholderDAO.getInstance());
	}

	@Test
	public void testGetFullKeyholders() {
		Assert.assertNotNull(KeyholderDAO.getInstance().getFullKeyholders(
				"NHSRGBQE01", 1426));
		// Assert.assertEquals(25,KeyholderDAO.getInstance().getFullKeyholders("NHSRGBQE01",
		// 1426).size());

		Assert.assertEquals(new ArrayList<Keyholder>(), KeyholderDAO
				.getInstance().getFullKeyholders(null, -1));
		Assert.assertEquals(new ArrayList<Keyholder>(), KeyholderDAO
				.getInstance().getFullKeyholders("abc", 1426));// for wrong
																// branch code
		Assert.assertNotNull(KeyholderDAO.getInstance().getFullKeyholders(
				"NHSRGBQE01", 3432423));// for wrong department id (returns all
										// keyholders with empty call list
										// details)
		Assert.assertEquals(new ArrayList<Keyholder>(), KeyholderDAO
				.getInstance().getFullKeyholders("abc", 3432423));// for wrong
																	// branch
																	// code and
																	// department
																	// id
	}

	@Test
	public void testGetKeyholdersTypes() {
		Assert.assertNotNull(
				"Call List types for special occasions are null",
				KeyholderDAO.getInstance().getKeyholdersTypes("NHSRGBQE01",
						1426, true));
		Assert.assertNotNull(
				"Call List types for other than special occasions are null",
				KeyholderDAO.getInstance().getKeyholdersTypes("NHSRGBQE01",
						1426, false));

		Assert.assertEquals(
				1,
				KeyholderDAO.getInstance()
						.getKeyholdersTypes("NHSRGBQE01", 1579, false).size());
		Assert.assertEquals(
				4,
				KeyholderDAO.getInstance()
						.getKeyholdersTypes("NHSRGBQE01", 1426, false).size());

		Assert.assertEquals(
				1,
				KeyholderDAO.getInstance()
						.getKeyholdersTypes("NHSRGBQE01", 1327, true).size());
		Assert.assertEquals(
				0,
				KeyholderDAO.getInstance()
						.getKeyholdersTypes("NHSRGBQE01", 1426, true).size());

		Assert.assertEquals(new ArrayList<Keyholder>(), KeyholderDAO
				.getInstance().getKeyholdersTypes(null, -1, false));
		Assert.assertEquals(new ArrayList<Keyholder>(), KeyholderDAO
				.getInstance().getKeyholdersTypes(null, -1, true));

		Assert.assertEquals(new ArrayList<Keyholder>(), KeyholderDAO
				.getInstance().getKeyholdersTypes("kjk", 1426, true));// wrong
																		// branch
																		// code
																		// should
																		// return
																		// empty
																		// list
		Assert.assertEquals(new ArrayList<Keyholder>(), KeyholderDAO
				.getInstance().getKeyholdersTypes("kjk", 1426, false));// wrong
																		// branch
																		// code
																		// should
																		// return
																		// empty
																		// list

		Assert.assertEquals(new ArrayList<Keyholder>(), KeyholderDAO
				.getInstance().getKeyholdersTypes("NHSRGBQE01", 14232436, true));// wrong
																					// department
																					// id
																					// should
																					// return
																					// empty
																					// list
		Assert.assertEquals(new ArrayList<Keyholder>(), KeyholderDAO
				.getInstance()
				.getKeyholdersTypes("NHSRGBQE01", 14232436, false));// wrong
																	// department
																	// id should
																	// return
																	// empty
																	// list

	}

	@Test
	public void testGetSpecialKeyholders() {
		Assert.assertNotNull(KeyholderDAO.getInstance().getSpecialKeyholders(
				"NHSRGBQE01", 1327));
		// Assert.assertEquals(19,KeyholderDAO.getInstance().getSpecialKeyholders("NHSRGBQE01",
		// 1327).size());

		Assert.assertNotNull(KeyholderDAO.getInstance().getSpecialKeyholders(
				"NHSRGBQE01", 1426));
		// Assert.assertEquals(19,KeyholderDAO.getInstance().getSpecialKeyholders("NHSRGBQE01",
		// 1426).size());

		Assert.assertEquals(new ArrayList<Keyholder>(), KeyholderDAO
				.getInstance().getSpecialKeyholders(null, -1));

		Assert.assertEquals(new ArrayList<Keyholder>(), KeyholderDAO
				.getInstance().getSpecialKeyholders("abc", 1327));// for wrong
																	// branch
																	// code
		Assert.assertNotNull(KeyholderDAO.getInstance().getSpecialKeyholders(
				"NHSRGBQE01", 3432423));// for wrong department id (returns all
										// keyholders with empty call list
										// details)
		Assert.assertEquals(new ArrayList<Keyholder>(), KeyholderDAO
				.getInstance().getSpecialKeyholders("abc", 3432423));// for
																		// wrong
																		// branch
																		// code
																		// and
																		// department
																		// id
	}

	@Test
	public void testGetListOfKeyholders() {
		Assert.assertNotNull(KeyholderDAO.getInstance().getListOfKeyholders(
				"NHSFPHTRA", 1));
		// Assert.assertEquals(8,
		// KeyholderDAO.getInstance().getListOfKeyholders("NHSFPHTRA",
		// 1).size());

		// wrong inputs
		Assert.assertEquals(new ArrayList<Keyholder>(), KeyholderDAO
				.getInstance().getListOfKeyholders("KLLJKL", 2));
		Assert.assertEquals(new ArrayList<Keyholder>(), KeyholderDAO
				.getInstance().getListOfKeyholders("NHSFPHTRA", 232));

		Assert.assertEquals(new ArrayList<Keyholder>(), KeyholderDAO
				.getInstance().getListOfKeyholders(null, -1));
	}

	@Test
	public void testGetKeyholderByIdStringIntInt() {
		Assert.assertNotNull(KeyholderDAO.getInstance().getKeyholderById(
				"NHSFPHTRA", 1, 60));
		Assert.assertNull(KeyholderDAO.getInstance().getKeyholderById(
				"dfsdfds", 1, -1));
		Assert.assertNull(KeyholderDAO.getInstance().getKeyholderById(
				"dfsdfds", -1, -1));
		Assert.assertNull(KeyholderDAO.getInstance().getKeyholderById(null, -1,
				-1));
	}

	@Test
	public void testGetKeyholderByIdStringInt() {
		Assert.assertNotNull(KeyholderDAO.getInstance().getKeyholderById(
				"B6404", 61));
		Assert.assertNull(KeyholderDAO.getInstance().getKeyholderById(
				"dfsdfds", -1));
		Assert.assertNull(KeyholderDAO.getInstance().getKeyholderById(null, -1));
	}

	@Test
	public void testUpdateKeyholdersList() {
		List<Keyholder> keyholders = new ArrayList<Keyholder>();
		List<Keyholder> originalKeyholders = new ArrayList<Keyholder>();
		originalKeyholders = KeyholderDAO.getInstance().getListOfKeyholders(
				"NHSFPHTRA", 1);
		// Assert.assertEquals(2,KeyholderDAO.getInstance().getListOfKeyholders("NHSFPHTRA",
		// 1).get(0).getPriority());

		Keyholder keyholder1 = new Keyholder();
		keyholder1.setKeyholderId(originalKeyholders.get(0).getKeyholderId());
		keyholder1.setPriority(1);
		keyholders.add(keyholder1);
		KeyholderDAO.getInstance().updateKeyholdersList(keyholders, 1);

		Assert.assertEquals(1,
				KeyholderDAO.getInstance().getListOfKeyholders("NHSFPHTRA", 1)
						.get(0).getPriority());

		// Rollback changes
		KeyholderDAO.getInstance().updateKeyholdersList(originalKeyholders, 1);
		// Assert.assertEquals(2,KeyholderDAO.getInstance().getListOfKeyholders("NHSFPHTRA",
		// 1).get(0).getPriority());

		KeyholderDAO.getInstance().updateKeyholdersList(null, 1);

		keyholders = new ArrayList<Keyholder>();
		keyholder1 = new Keyholder();
		keyholder1.setKeyholderId(20);
		keyholder1.setPriority(13);
		keyholders.add(keyholder1);

		KeyholderDAO.getInstance().updateKeyholdersList(keyholders, -1);

		KeyholderDAO.getInstance().updateKeyholdersList(null, -1);

	}

	@Test
	public void testGetAllNormalLists() {
		Assert.assertNotNull(KeyholderDAO.getInstance().getAllNormalLists(3));
		// Assert.assertEquals(7,
		// KeyholderDAO.getInstance().getAllNormalLists(3).size());

		Assert.assertEquals(new ArrayList<KeyholderList>(), KeyholderDAO
				.getInstance().getAllNormalLists(-1));

	}

	@Test
	public void testGetAllHolidayLists() {
		Assert.assertNotNull(KeyholderDAO.getInstance().getAllHolidayLists(3));
		// Assert.assertEquals(3,
		// KeyholderDAO.getInstance().getAllHolidayLists(1234).size());

		Assert.assertEquals(new ArrayList<KeyholderList>(), KeyholderDAO
				.getInstance().getAllNormalLists(-1));
	}

	@Test
	public void testGetContactTypes() {
		Assert.assertNotNull(KeyholderDAO.getInstance().getContactTypes(27));
		// Assert.assertEquals(2,
		// KeyholderDAO.getInstance().getContactTypes(27).size());

		Assert.assertEquals(new ArrayList<Keyholder>(), KeyholderDAO
				.getInstance().getContactTypes(-1));
	}

	@Test
	public void testSaveKeyholderList_Keyholder() {
		Keyholder keyholder = KeyholderDAO.getInstance().getKeyholderById(
				"NHSFPHTRA", 53460);
		List<Keyholder> contactTypes = KeyholderDAO.getInstance()
				.getContactTypes(53460);
		int id = keyholder.getKeyholderListIds().get(0);
		KeyholderListEnablingDetails details = new KeyholderListEnablingDetails();
		details.setContact_type(3);
		details.setKeyholderListId(id);
		KeyholderDAO.getInstance().saveKeyholderList_Keyholder(keyholder,
				details);

		List<Keyholder> newContactTypes = KeyholderDAO.getInstance()
				.getContactTypes(53460);
		Keyholder keyholder2Check = null;
		for (Keyholder keyholder2 : newContactTypes) {
			if (keyholder2.getKeyholderId() == 53460
					&& keyholder2.getKeyholderListId() == id) {
				keyholder2Check = keyholder2;
				break;
			}
		}
		Assert.assertEquals(3, keyholder2Check.getContact_type());

		// Rollback changes
		Keyholder keyholder2Apply = null;
		for (Keyholder keyholder2 : contactTypes) {
			if (keyholder2.getKeyholderId() == 53460
					&& keyholder2.getKeyholderListId() == id) {
				keyholder2Apply = keyholder2;
				break;
			}
		}
		details = new KeyholderListEnablingDetails();
		details.setContact_type(keyholder2Apply.getContact_type());
		details.setKeyholderListId(id);
		KeyholderDAO.getInstance().saveKeyholderList_Keyholder(keyholder,
				details);
	}

	@Test
	public void testDeleteKeyholderList_Keyholder() {
		Keyholder keyholder = KeyholderDAO.getInstance().getKeyholderById(
				"NHSFPHTRA", 53460);

		KeyholderListEnablingDetails details = new KeyholderListEnablingDetails();
		details.setContact_type(3);
		details.setKeyholderListId(1);
		KeyholderDAO.getInstance().saveKeyholderList_Keyholder(keyholder,
				details);

		KeyholderDAO.getInstance().deleteKeyholderList_Keyholder(keyholder,
				details);

		Keyholder newKeyholder = KeyholderDAO.getInstance().getKeyholderById(
				"NHSFPHTRA", 53460);
		int id2Check = -1;
		for (int id : newKeyholder.getKeyholderListIds()) {
			if (id == 1) {
				id2Check = id;
			}
		}
		Assert.assertEquals(-1, id2Check);
	}

	@Test
	public void testUpdateSiteKeyholder() {
		Keyholder originalKeyholder = KeyholderDAO.getInstance()
				.getKeyholderById("NHSFPHTRA", 27);

		Keyholder newKeyholder = new Keyholder();
		newKeyholder = new Keyholder(originalKeyholder.getKeyholderId(),
				"new user", originalKeyholder.getPosition(),
				originalKeyholder.isActive(), originalKeyholder.getPhone(),
				originalKeyholder.getMobile(), originalKeyholder.getFax(),
				originalKeyholder.getEmail(),
				originalKeyholder.getBranchCode(), 0, false);
		newKeyholder.setKeyholderList(originalKeyholder.getKeyholderList());
		newKeyholder.setKeyholderListIds(originalKeyholder
				.getKeyholderListIds());

		KeyholderDAO.getInstance().updateSiteKeyholder(newKeyholder);
		Assert.assertEquals("new user", KeyholderDAO.getInstance()
				.getKeyholderById("NHSFPHTRA", 27).getContactName());

		// rollback changes
		KeyholderDAO.getInstance().updateSiteKeyholder(originalKeyholder);
	}

	@Test
	public void testAddBranchKeyholder() {
		int keyholderId = KeyholderDAO.getInstance().addBranchKeyholder(
				"testName", "Mobile", true, "324324", "3432423", "3243243",
				"test@abc.com", "TFFRMPTN");

		Keyholder keyholder = KeyholderDAO.getInstance().getKeyholderById(
				"TFFRMPTN", keyholderId);
		Assert.assertNotNull(keyholder);
		Assert.assertEquals("testName", keyholder.getContactName());

		// rollback changes
		KeyholderDAO.getInstance().deleteBranchKeyholder(keyholderId);

	}

	@Test
	public void testDeleteBranchKeyholder() {
		int keyholderId = KeyholderDAO.getInstance().addBranchKeyholder(
				"testName", "Mobile", true, "324324", "3432423", "3243243",
				"test@abc.com", "TFFRMPTN");
		KeyholderDAO.getInstance().deleteBranchKeyholder(keyholderId);

		Assert.assertNull(KeyholderDAO.getInstance().getKeyholderById(
				"TFFRMPTN", keyholderId));
	}

	@Test
	public void testRetrieveDepartmentKeyholderListNormal() {
		Assert.assertNotNull(KeyholderDAO.getInstance()
				.retrieveDepartmentKeyholderListNormal(1234));
		// Assert.assertEquals(2,
		// KeyholderDAO.getInstance().retrieveDepartmentKeyholderListNormal(1234).size());

		Assert.assertEquals(new ArrayList<KeyholderList>(), KeyholderDAO
				.getInstance().retrieveDepartmentKeyholderListNormal(-1));
	}

	@Test
	public void testRetrieveDepartmentKeyholderListHoliday() {
		Assert.assertNotNull(KeyholderDAO.getInstance()
				.retrieveDepartmentKeyholderListHoliday(1234));
		// Assert.assertEquals(3,
		// KeyholderDAO.getInstance().retrieveDepartmentKeyholderListHoliday(1234).size());

		Assert.assertEquals(new ArrayList<KeyholderList>(), KeyholderDAO
				.getInstance().retrieveDepartmentKeyholderListHoliday(-1));
	}

	@Test
	public void testAddDeptKeyholderList() {
		KeyholderDAO.getInstance().addDeptKeyholderList("testingDepartment",
				"its a test department", "testDepartment", "my own type",
				new Date(), new Date(), "01:05", "02:05", "", 1234, "1,2,3");
		List<KeyholderList> list = KeyholderDAO.getInstance()
				.retrieveDepartmentKeyholderListHoliday(1234);
		KeyholderList newKeyholderList = null;
		for (int i = 0; i < list.size(); i++) {
			if ("testingDepartment".equals(list.get(i).getDisplayName())) {
				newKeyholderList = list.get(i);
			}
		}
		Assert.assertNotNull(newKeyholderList);
		Assert.assertEquals("testingDepartment",
				newKeyholderList.getDisplayName());
		Assert.assertEquals("its a test department",
				newKeyholderList.getDescription());
		Assert.assertEquals("testDepartment", newKeyholderList.getListName());
		Assert.assertEquals("my own type", newKeyholderList.getListType());
		Assert.assertEquals("01:05", newKeyholderList.getOccupancyStart());
		Assert.assertEquals("02:05", newKeyholderList.getOccupancyEnd());
		Assert.assertEquals("1,2,3", newKeyholderList.getWeekdaysActive());

		// Rollback changes
		KeyholderDAO.getInstance().delDeptKeyholderList(
				newKeyholderList.getKeyholderListId());
	}

	@Test
	public void testDelDeptKeyholderList() {
		KeyholderDAO.getInstance().addDeptKeyholderList("testingDepartment",
				"its a test department", "testDepartment", "my own type",
				new Date(), new Date(), "01:05", "02:05", "", 1234, "1,2,3");
		List<KeyholderList> list = KeyholderDAO.getInstance()
				.retrieveDepartmentKeyholderListHoliday(1234);
		KeyholderList newKeyholderList = null;
		for (int i = 0; i < list.size(); i++) {
			if ("testingDepartment".equals(list.get(i).getDisplayName())) {
				newKeyholderList = list.get(i);
			}
		}

		KeyholderDAO.getInstance().delDeptKeyholderList(
				newKeyholderList.getKeyholderListId());

		KeyholderList keyholderList = KeyholderDAO.getInstance()
				.getKeyholderListById(newKeyholderList.getKeyholderListId());

		Assert.assertNull(keyholderList);

	}

	@Test
	public void testModifyKeyholderList() {
		List<KeyholderList> list = KeyholderDAO.getInstance()
				.retrieveDepartmentKeyholderListHoliday(1234);
		KeyholderList keyholderList = list.get(0);
		KeyholderDAO.getInstance().modifyKeyholderList("newListName",
				keyholderList.getListType(), keyholderList.getStartDate(),
				keyholderList.getEndDate(), keyholderList.getOccupancyStart(),
				keyholderList.getOccupancyEnd(), keyholderList.getComments(),
				keyholderList.getKeyholderListId(),
				keyholderList.getWeekdaysActive(),
				keyholderList.getDescription(), keyholderList.getDisplayName());

		KeyholderList newKeyholderList = KeyholderDAO.getInstance()
				.getKeyholderListById(keyholderList.getKeyholderListId());
		Assert.assertEquals("newListName", newKeyholderList.getListName());

		// Rollback changes
		KeyholderDAO.getInstance().modifyKeyholderList(
				keyholderList.getListName(), keyholderList.getListType(),
				keyholderList.getStartDate(), keyholderList.getEndDate(),
				keyholderList.getOccupancyStart(),
				keyholderList.getOccupancyEnd(), keyholderList.getComments(),
				keyholderList.getKeyholderListId(),
				keyholderList.getWeekdaysActive(),
				keyholderList.getDescription(), keyholderList.getDisplayName());
	}
	
}
