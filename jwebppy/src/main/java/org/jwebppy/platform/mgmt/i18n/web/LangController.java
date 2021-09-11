package org.jwebppy.platform.mgmt.i18n.web;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.MgmtGeneralController;
import org.jwebppy.platform.mgmt.i18n.dto.LangDetailDto;
import org.jwebppy.platform.mgmt.i18n.dto.LangDto;
import org.jwebppy.platform.mgmt.i18n.dto.LangKindDto;
import org.jwebppy.platform.mgmt.i18n.dto.LangSearchDto;
import org.jwebppy.platform.mgmt.i18n.service.LangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/platform/mgmt/i18n")
public class LangController extends MgmtGeneralController
{
	@Autowired
	private LangService langService;

	@RequestMapping("/list")
	public String list()
	{
		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/list/data")
	@ResponseBody
	public Object listData(@ModelAttribute LangSearchDto langSearch)
	{
		LangKindDto langKind = new LangKindDto();
		langKind.setBasename(langSearch.getBasename());

		return LangLayoutBuilder.getList(new PageableList<>(langService.getPageableLangs(langSearch)), langService.getLangKinds(langKind));
	}

	@GetMapping("/write")
	@ResponseBody
	public Object write(@ModelAttribute LangSearchDto langSearch)
	{
		List<String> basenames = langService.getBasenames();

		LangDto lang = null;

		if (langSearch.getLSeq() != null)
		{
			LangDto pLang = new LangDto();
			pLang.setLSeq(langSearch.getLSeq());

			lang = langService.getLang(pLang);
		}
		else
		{
			if (CmStringUtils.isNotEmpty(langSearch.getBasename()))
			{
				List<LangDetailDto> langDetails = langService.getLangDetails(langSearch);

				if (CollectionUtils.isNotEmpty(langDetails))
				{
					lang = langService.getLangByLSeq(langDetails.get(0).getLSeq());
				}
			}
		}

		if (lang == null)
		{
			lang = new LangDto();
			lang.setType(langSearch.getType());
			lang.setSeq(langSearch.getSeq());
			lang.setBasename(CmStringUtils.defaultIfEmpty(langSearch.getBasename(), basenames.get(0)));//default PLTF
		}

		LangKindDto langKind = new LangKindDto();
		langKind.setBasename(lang.getBasename());

		return LangLayoutBuilder.getLangForm(basenames, langService.getLangKinds(langKind), lang, langSearch.getFrom());
	}

	@GetMapping("/valid_check")
	@ResponseBody
	public Object validCheck(@ModelAttribute LangDto lang)
	{
		if (langService.isDuplicated(lang))
		{
			return "DUPLICATED";
		}

		return EMPTY_RETURN_VALUE;
	}

	@PostMapping("/save")
	@ResponseBody
	public Object save(@ModelAttribute LangDto lang, @RequestParam(value = "lkSeq") List<Integer> lkSeqs, @RequestParam(value = "text") String[] texts)
	{
		List<LangDetailDto> langDetails = new LinkedList<>();

		int index = 0;
		for (Integer lkSeq : lkSeqs)
		{
			LangDetailDto langDetail = new LangDetailDto();
			langDetail.setLkSeq(lkSeq);
			langDetail.setText(texts[index++]);

			langDetails.add(langDetail);
		}

		lang.setLangDetails(langDetails);

		return langService.save(lang);
	}

	@PostMapping("/delete")
	@ResponseBody
	public Object delete(@RequestParam(value = "lSeq") List<Integer> lSeqs)
	{
		LangDto lang = new LangDto();
		lang.setLSeqs(lSeqs);

		return langService.delete(lang);
	}

	@GetMapping("/basenames")
	@ResponseBody
	public Object basenames(@ModelAttribute LangSearchDto langSearch)
	{
		return langService.getBasenames();
	}
}
