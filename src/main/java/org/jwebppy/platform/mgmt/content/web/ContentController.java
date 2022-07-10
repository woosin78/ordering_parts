package org.jwebppy.platform.mgmt.content.web;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.cache.CacheClear;
import org.jwebppy.platform.core.util.CmAnnotationUtils;
import org.jwebppy.platform.core.util.CmClassUtils;
import org.jwebppy.platform.core.util.CmReflectionUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.dom.Document;
import org.jwebppy.platform.mgmt.content.ContentGeneralController;
import org.jwebppy.platform.mgmt.content.dto.CItemComponentDto;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemLangRlDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.service.ContentLangService;
import org.jwebppy.platform.mgmt.content.service.ContentService;
import org.jwebppy.platform.mgmt.i18n.dto.LangDto;
import org.jwebppy.platform.mgmt.i18n.dto.LangKindDto;
import org.jwebppy.platform.mgmt.i18n.service.LangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PlatformConfigVo.CONTEXT_PATH + "/mgmt/content")
public class ContentController extends ContentGeneralController
{
	@Autowired
	private ContentLangService contentLangService;

	@Autowired
	private ContentService contentService;

	@Autowired
	private LangService langService;

	@RequestMapping("/view")
	public String view(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/popup/view")
	public String viewPopup(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/list/layout")
	@ResponseBody
	public Object listLayout(@ModelAttribute CItemSearchDto cItemSearch)
	{
		return contentService.getCItems(cItemSearch);
	}

	@GetMapping("/layout/{tabPath}")
	@ResponseBody
	public Object viewLayout(@PathVariable("tabPath") String tabPath, @ModelAttribute CItemSearchDto cItemSearch)
	{
		if ("general".equals(tabPath))
		{
			CItemDto cItem = (cItemSearch.getCSeq() == null) ? contentService.getRoot() : contentService.getCItem(cItemSearch.getCSeq());

			return ContentLayoutBuilder.viewGeneralInfo(cItem);
		}
		else
		{
			CItemLangRlDto cItemLangRl = new CItemLangRlDto();
			cItemLangRl.setCSeq(cItemSearch.getCSeq());
			cItemLangRl.setBasename(cItemSearch.getBasename());

			List<CItemLangRlDto> cItemLangRls = contentLangService.getCItemLangRls(cItemLangRl);

			if (CollectionUtils.isNotEmpty(cItemLangRls))
			{
				LangDto lang = langService.getLangByLSeq(cItemLangRls.get(0).getLSeq());

				LangKindDto langKind = new LangKindDto();
				langKind.setBasename(lang.getBasename());

				return ContentLayoutBuilder.viewLang(langService.getBasenames(), langService.getLangKinds(langKind), lang);
			}
		}

		return Document.EMPTY;
	}

	@GetMapping("/write/layout/{tabPath}")
	@ResponseBody
	public Object writeLayout(@PathVariable("tabPath") String tabPath, @ModelAttribute CItemSearchDto cItemSearch)
	{
		CItemDto cItem = (cItemSearch.getCSeq() != null) ? contentService.getCItem(cItemSearch.getCSeq()) : new CItemDto();
		CItemDto parentCItem = (cItemSearch.getPSeq() != null) ? contentService.getCItem(cItemSearch.getPSeq()) : new CItemDto();

		return ContentLayoutBuilder.writeGeneralInfo(cItem, parentCItem,  getComponents(), getEntryPoints(cItem.getComponent()));
	}

	@PostMapping("/save")
	@CacheClear(name = "A")
	@ResponseBody
	public Object save(@ModelAttribute CItemDto cItem)
	{
		cItem.setName(cItem.getName().toUpperCase());

		return contentService.save(cItem);
	}

	@PostMapping("/delete")
	@CacheClear(name = "A")
	@ResponseBody
	public Object delete(@RequestParam("cSeq") Integer cSeq)
	{
		return contentService.delete(cSeq);
	}

	@PostMapping("/copy")
	@CacheClear(name = "A")
	@ResponseBody
	public Object copy(@RequestParam("cSeq") Integer cSeq, @RequestParam("pSeq") Integer pSeq)
	{
		return contentService.copy(cSeq, pSeq, PlatformCommonVo.NO);
	}

	@PostMapping("/copy_with_sub_items")
	@CacheClear(name = "A")
	@ResponseBody
	public Object copyWithSubItems(@RequestParam("cSeq") Integer cSeq, @RequestParam("pSeq") Integer pSeq)
	{
		return contentService.copy(cSeq, pSeq, PlatformCommonVo.YES);
	}

	@PostMapping("/move")
	@CacheClear(name = "A")
	@ResponseBody
	public Object move(@RequestParam("cSeq") Integer cSeq, @RequestParam("pSeq") Integer pSeq)
	{
		return contentService.move(cSeq, pSeq);
	}

	@PostMapping("/lang/save")
	@CacheClear(name = "A")
	@ResponseBody
	public Object saveLang(@ModelAttribute CItemLangRlDto cItemLangRl)
	{
		return contentLangService.save(cItemLangRl);
	}

	@GetMapping("/lang")
	@ResponseBody
	public Object lang(@ModelAttribute CItemLangRlDto pCItemLangRl)
	{
		List<CItemLangRlDto> cItemLangRls = contentLangService.getCItemLangRls(pCItemLangRl);

		if (CollectionUtils.isNotEmpty(cItemLangRls))
		{
			return langService.getLangByLSeq(cItemLangRls.get(0).getLSeq());
		}

		return null;
	}

	@GetMapping("/tree")
	@ResponseBody
	public Object itemsHierarchy(@ModelAttribute CItemSearchDto cItemSearch)
	{
		if (ObjectUtils.isEmpty(cItemSearch.getCSeq()))
		{
			cItemSearch.setCSeq(contentService.getRoot().getCSeq());
		}

		return contentService.getCItemHierarchy2(cItemSearch);
	}

	@GetMapping("/component/entry_points")
	@ResponseBody
	public Object entryPoints(@ModelAttribute CItemSearchDto cItemSearch)
	{
		return getEntryPoints(cItemSearch.getComponent());
	}

	@GetMapping("/valid_check/{field}")
	@ResponseBody
	public Object validCheck(@PathVariable("field") String field, @RequestParam("value") String value, @RequestParam(required = false, value = "cSeq") Integer cSeq)
	{
		CItemSearchDto cItemSearch = new CItemSearchDto();
		cItemSearch.setName(value.toUpperCase());

		List<CItemDto> cItems = contentService.getCItems(cItemSearch);

		if (CollectionUtils.isEmpty(cItems))
		{
			return EMPTY_RETURN_VALUE;
		}

		if (cSeq != null)
		{
			for (CItemDto cItem: cItems)
			{
				if (cSeq.equals(cItem.getCSeq()))
				{
					return EMPTY_RETURN_VALUE;
				}
			}
		}

		return "duplicated";
	}

	private List<CItemComponentDto> getComponents()
	{
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(Controller.class));

		List<CItemComponentDto> itemComponents = new LinkedList<>();

		for (BeanDefinition beanDefinition : scanner.findCandidateComponents("org.jwebppy"))
		{
		    try
		    {
		    	Class<?> clazz = CmClassUtils.getClass(beanDefinition.getBeanClassName());

				CItemComponentDto cItemComponent = new CItemComponentDto();
				cItemComponent.setClassName(clazz.getName());

				itemComponents.add(cItemComponent);
			}
		    catch (ClassNotFoundException e)
		    {
				e.printStackTrace();
			}
		}

		return itemComponents;
	}

	private List<CItemComponentDto> getEntryPoints(String component)
	{
		if (CmStringUtils.isNotEmpty(component))
		{
			Class<?> clazz = null;

			try
			{
				clazz = CmClassUtils.getClass(component);
			}
			catch (ClassNotFoundException e)
			{
				return null;
			}

			Method[] methods = CmReflectionUtils.getAllDeclaredMethods(clazz);

			if (methods == null)
			{
				return null;
			}

			List<CItemComponentDto> cItemComponents = new LinkedList<>();

    		for (Method method : methods)
    		{
    			Annotation[] annotations = method.getAnnotations();

    			if (annotations == null)
    			{
    				continue;
    			}

    			if (method.isAnnotationPresent(RequestMapping.class) && !method.isAnnotationPresent(ResponseBody.class))
    			{
					String url = getReturnUrl(clazz, method);

					if (CmStringUtils.isNotEmpty(url))
					{
						CItemComponentDto cItemComponent = new CItemComponentDto();
						cItemComponent.setClassName(clazz.getName());
						cItemComponent.setMethodName(method.getName());
						cItemComponent.setUrl(url);

						cItemComponents.add(cItemComponent);
					}
    			}
	    	}

    		return cItemComponents;
		}

		return null;
	}

	protected String getReturnUrl(Class<?> clazz, Method method)
	{
		if (CmAnnotationUtils.isAnnotationDeclaredLocally(RequestMapping.class, clazz))
		{
	        String value1 = CmStringUtils.trimToEmpty(CmAnnotationUtils.getAnnotationValue(CmAnnotationUtils.getAnnotation(clazz, RequestMapping.class)));
			String value2 = CmStringUtils.trimToEmpty(CmAnnotationUtils.getAnnotationValue(CmAnnotationUtils.getAnnotation(method, RequestMapping.class)));

			return value1 + value2;
		}

		return null;
	}
}
