package org.jwebppy.portal.iv.eu.parts.account.web;

import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.eu.parts.order.OrderGeneralController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("/portal/corp/eu/scm/parts/account")
public class EuAccountStatementController extends OrderGeneralController {
//	<!-- EPCF: Component com.doosaninfracore.parts.account.statementofaccount.StatementOfAccount, bcneiojigmmkjokmjndepphagbcgmbii -->

/*
 EXCEL

	I_BGTYP P
	I_BUDAT 20200220
	I_USERID P_EUFL01
*/

	@RequestMapping("/account_status")
	public String accountStatus(Model model, WebRequest webRequest)
	{
		if (CmStringUtils.isEmpty(webRequest.getParameter("pDate")))
		{
			model.addAttribute("pDate", CmDateFormatUtils.today());
		}

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}
}
