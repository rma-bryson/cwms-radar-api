package cwms.radar.data.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import cwms.radar.data.dto.Clob;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.MockConnection;
import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockExecuteContext;
import org.jooq.tools.jdbc.MockResult;
import org.junit.jupiter.api.Test;

import usace.cwms.db.jooq.codegen.packages.CWMS_ENV_PACKAGE;
import usace.cwms.db.jooq.codegen.tables.AV_CLOB;
import usace.cwms.db.jooq.codegen.tables.AV_OFFICE;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ClobDaoTest
//		extends DaoTest
{


//	@Test
//	public void testGetOne() throws SQLException
//	{
//		try(DSLContext lrl = getDslContext(getConnection(), "LRL"))
//		{
//			ClobDao dao = new ClobDao(lrl);
//			Optional<Clob> found = dao.getByUniqueName("/DATAEXCHANGE/OUTGOING-CONFIGURATION", Optional.of("LRL"));
//			Clob Clob = found.get();
//			assertNotNull(Clob);
//			assertNotNullOrEmpty(Clob.getDescription());
//			assertNotNullOrEmpty(Clob.getId());
//			assertNotNullOrEmpty(Clob.getValue());
//			assertNotNullOrEmpty(Clob.getOffice());
//		}
//
//	}
//
//	private void assertNotNullOrEmpty(String str){
//		assertNotNull(str);
//		assertFalse(str.isEmpty());
//	}
//
//	@Test
//	public void testGetLike() throws SQLException
//	{
//		try(DSLContext lrl = getDslContext(getConnection(), "LRL"))
//		{
//			ClobDao dao = new ClobDao(lrl);
//			List<Clob> found = dao.getClobsLike("LRL", "HEADERPANELID.LR%ELEV%");
//			assertNotNull(found);
//			assertFalse(found.isEmpty());
//		}
//
//	}
//
//	@Test
//	public void testGetValue() throws SQLException
//	{
//		try(DSLContext lrl = getDslContext(getConnection(), "LRL"))
//		{
//			ClobDao dao = new ClobDao(lrl);
//			String value = dao.getClobValue("LRL", "/DATAEXCHANGE/OUTGOING-CONFIGURATION");
//			assertNotNull(value);
//			assertFalse(value.isEmpty());
//		}
//
//	}

	public static DSLContext getDslContext(Connection database, String officeId)
	{
		DSLContext dsl =  DSL.using(database, SQLDialect.ORACLE11G);
		CWMS_ENV_PACKAGE.call_SET_SESSION_OFFICE_ID(dsl.configuration(), officeId);
		return dsl;
	}

	@Test
	public void testGetValueMock() throws SQLException
	{

		MockDataProvider provider = new MockDataProvider() {
			public MockResult[] execute(MockExecuteContext context) throws SQLException {

				DSLContext dsl = DSL.using(SQLDialect.ORACLE12C);

				AV_CLOB ac = AV_CLOB.AV_CLOB;
				AV_OFFICE ao = AV_OFFICE.AV_OFFICE;

				// This is sensitive to the order.  Not sure why?  Found it out by trial an error.
				final Collection<? extends Field<?>> fields = Arrays.asList(
						ao.OFFICE_ID, ao.OFFICE_CODE, ac.OFFICE_CODE, ac.ID, ac.DESCRIPTION, ac.VALUE
				);
				Result result = dsl.newResult(fields);
				Record e = dsl.newRecord(fields);
				e = e.with(ao.OFFICE_ID, "LRL")
						.with(ao.OFFICE_CODE, 55l)
						.with(ac.ID, "IDNotInDb")
						.with(ac.VALUE, "Value:asdfasdf")
						.with(ac.DESCRIPTION, "Desc:I'm not really here")
						.with(ac.OFFICE_CODE, 55l)
				;


				result.add(e);

				// Now, return 1-many results, depending on whether this is
				// a batch/multi-result context
				return new MockResult[] {
						new MockResult(1, result)
				};
			}
		};
		Connection connection = new MockConnection(provider);


		try(DSLContext lrl = getDslContext(connection, "LRL"))
		{
			ClobDao dao = new ClobDao(lrl);
			Optional<Clob> opAc = dao.getByUniqueName("doesnt matter what is here", Optional.of("LRL"));
			Clob Clob = opAc.get();
			String value = Clob.getValue();
			assertNotNull(value);
			assertFalse(value.isEmpty());
		}

	}


}