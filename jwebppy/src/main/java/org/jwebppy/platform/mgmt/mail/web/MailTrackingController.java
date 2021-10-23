package org.jwebppy.platform.mgmt.mail.web;

import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.security.AES256Cipher;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.MgmtGeneralController;
import org.jwebppy.platform.mgmt.mail.service.MailReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mail")
public class MailTrackingController extends MgmtGeneralController
{
	@Autowired
	private MailReceiverService mailReceiverService;

	@GetMapping("/tracking")
	@ResponseBody
	public void tracking(@RequestParam(value = "key") String key)
	{
		String[] values = CmStringUtils.split(AES256Cipher.getInstance().decode(key), PlatformConfigVo.DELIMITER);

		if (values.length == 2)
		{
			mailReceiverService.modifyReadDate(Integer.parseInt(values[0]), values[1]);
		}
	}
}
