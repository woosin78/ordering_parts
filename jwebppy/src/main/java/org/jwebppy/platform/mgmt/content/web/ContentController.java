package org.jwebppy.platform.mgmt.content.web;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmAnnotationUtils;
import org.jwebppy.platform.core.util.CmClassUtils;
import org.jwebppy.platform.core.util.CmReflectionUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.core.web.ui.dom.Document;
import org.jwebppy.platform.mgmt.content.ContentGeneralController;
import org.jwebppy.platform.mgmt.content.dto.CItemComponentDto;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemLangRlDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.service.ContentAuthorityService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/platform/mgmt/content")
public class ContentController extends ContentGeneralController
{
	@Autowired
	private ContentAuthorityService contentAuthorityService;

	@Autowired
	private ContentLangService contentLangService;

	@Autowired
	private ContentService contentService;

	@Autowired
	private LangService langService;

	@RequestMapping("/main")
	public String main()
	{
		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/contents")
	@ResponseBody
	public Object contents(@ModelAttribute CItemSearchDto cItemSearch)
	{
		return contentService.getAllItems(cItemSearch);
	}

	@GetMapping("/{tabPath}")
	@ResponseBody
	public Object view(@PathVariable("tabPath") String tabPath, @ModelAttribute CItemSearchDto cItemSearch)
	{
		if ("general".equals(tabPath))
		{
			return ContentLayoutBuilder.getGeneralInfo(contentService.getItem(cItemSearch.getCSeq()));
		}
		else
		{
			CItemLangRlDto cItemLangRl = new CItemLangRlDto();
			cItemLangRl.setCSeq(cItemSearch.getCSeq());
			cItemLangRl.setBasename(cItemSearch.getBasename());

			List<CItemLangRlDto> cItemLangRls = contentLangService.getLangs(cItemLangRl);

			if (CollectionUtils.isNotEmpty(cItemLangRls))
			{
				LangDto lang = langService.getLangByLSeq(cItemLangRls.get(0).getLSeq());

				LangKindDto langKind = new LangKindDto();
				langKind.setBasename(lang.getBasename());

				return ContentLayoutBuilder.getLang(langService.getBasenames(), langService.getLangKinds(langKind), lang);
			}
		}

		return Document.EMPTY;
	}

	@GetMapping("/modify/{tabPath}")
	@ResponseBody
	public Object modify(@PathVariable("tabPath") String tabPath, @ModelAttribute CItemSearchDto cItemSearch)
	{
		CItemDto cItem = new CItemDto();

		if (cItemSearch.getCSeq() != null)
		{
			cItem = contentService.getItem(cItemSearch.getCSeq());
		}

		return ContentLayoutBuilder.getGeneralInfoForm(cItem, getComponents(), getEntryPoints(cItem.getComponent()));
	}

	@PostMapping("/save/{tabPath}")
	@ResponseBody
	public Object save(@PathVariable("tabPath") String tabPath, @ModelAttribute CItemDto cItem)
	{
		return contentService.save(cItem);
	}

	@PostMapping("/lang/save")
	@ResponseBody
	public Object saveLang(@ModelAttribute CItemLangRlDto cItemLangRl)
	{
		return contentLangService.save(cItemLangRl);
	}

	@GetMapping("/lang")
	@ResponseBody
	public Object lang(@ModelAttribute CItemLangRlDto pCItemLangRl)
	{
		List<CItemLangRlDto> citemLangRls = contentLangService.getLangs(pCItemLangRl);

		if (CollectionUtils.isNotEmpty(citemLangRls))
		{
			return langService.getLangByLSeq(citemLangRls.get(0).getLSeq());
		}

		return null;
	}

	@PostMapping("/delete")
	@ResponseBody
	public Object delete(@RequestParam("cSeq") Integer cSeq)
	{
		return contentService.delete(cSeq);
	}

	@GetMapping("/tree")
	@ResponseBody
	public Object itemsHierarchy(@ModelAttribute CItemSearchDto cItemSearch)
	{
		UserAuthenticationUtils.getUserDetails().getLanguage();

		List<Map<String, Object>> items = new ArrayList<>();
		items.add(contentService.makeHierarchy(cItemSearch));

		return items;
	}

	@GetMapping("/component/entry_points")
	@ResponseBody
	public Object entryPoints(@ModelAttribute CItemSearchDto cItemSearch)
	{
		return getEntryPoints(cItemSearch.getComponent());
	}

	@GetMapping("/my_menu")
	@ResponseBody
	public Object myMenu(@ModelAttribute CItemSearchDto cItemSearch)
	{
		List<Map<String, Object>> cItemsHierarchy = new LinkedList<>();

		String lang = UserAuthenticationUtils.getUserDetails().getLanguage();

		cItemSearch.setUSeq(getUSeq());
		cItemSearch.setLang(lang);

		List<CItemDto> cItems = contentAuthorityService.getMyItemHierarchy(cItemSearch);

		for (CItemDto cItem : cItems)
		{
			Map<String, Object> itemMap = new LinkedHashMap<>();
			itemMap.put("KEY", cItem.getCSeq());
			itemMap.put("NAME", CmStringUtils.defaultIfEmpty(cItem.getName2(), cItem.getName()));
			itemMap.put("TYPE", cItem.getType().toString());
			itemMap.put("URL", CmStringUtils.trimToEmpty(cItem.getUrl()));

			itemMap.put("SUB_ITEMS", getSubItems(cItem.getSubCItems(), lang));

			cItemsHierarchy.add(itemMap);
		}

		return cItemsHierarchy;
	}

	protected List<Map<String, Object>> getSubItems(List<CItemDto> subCItems, String lang)
	{
		List<Map<String, Object>> cItems = new LinkedList<>();

		if (CollectionUtils.isNotEmpty(subCItems))
		{
			for (CItemDto subCItem: subCItems)
			{
				Map<String, Object> itemMap = new LinkedHashMap<>();
				itemMap.put("KEY", subCItem.getCSeq());
				itemMap.put("NAME", langService.getCItemText("PLTF", subCItem.getCSeq(), lang));
				itemMap.put("TYPE", subCItem.getType().toString());
				itemMap.put("URL", subCItem.getUrl());
				itemMap.put("SUB_ITEMS", getSubItems(subCItem.getSubCItems(), lang));

				cItems.add(itemMap);
			}
		}

		return cItems;
	}

	@GetMapping("/breadcrumb")
	@ResponseBody
	public Object breadcrumb(CItemSearchDto cItemSearch)
	{
		cItemSearch.setUSeq(getUSeq());
		cItemSearch.setLang(UserAuthenticationUtils.getUserDetails().getLanguage());

		List<CItemDto> cItems = contentAuthorityService.getMyItemHierarchy(cItemSearch);

		if (CollectionUtils.isNotEmpty(cItems))
		{
			for (CItemDto cItem: cItems)
			{
				List<CItemDto> breadcrumb = new ArrayList<>();

				if (isFindEntryPoint(breadcrumb, cItem, cItemSearch.getEntryPoint()))
				{
					Collections.reverse(breadcrumb);

					return breadcrumb;
				}
			}
		}

		return null;
	}

	private boolean isFindEntryPoint(List<CItemDto> breadcrumb, CItemDto cItem, String url)
	{
		if (CollectionUtils.isNotEmpty(cItem.getSubCItems()))
		{
			for (CItemDto subCItem: cItem.getSubCItems())
			{
				if (!CmStringUtils.equals(PlatformCommonVo.PAGE, subCItem.getType()) && !CmStringUtils.equals(PlatformCommonVo.MEMU, subCItem.getType()))
				{
					continue;
				}

				if (CmStringUtils.equals(PlatformCommonVo.PAGE, subCItem.getType()) && CmStringUtils.notEquals(url, subCItem.getUrl()))
				{
					continue;
				}

				breadcrumb.add(subCItem);

				if (CmStringUtils.equals(url, subCItem.getUrl()))
				{
					return true;
				}

				if (isFindEntryPoint(breadcrumb, subCItem, url))
				{
					return true;
				}
			}
		}

		return false;
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

    			if (!method.isAnnotationPresent(ResponseBody.class))
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
	        String value1 = CmAnnotationUtils.getAnnotationValue(CmAnnotationUtils.getAnnotation(clazz, RequestMapping.class));
			String value2 = CmAnnotationUtils.getAnnotationValue(CmAnnotationUtils.getAnnotation(method, RequestMapping.class));

			if (CmStringUtils.isNotEmpty(value1) && CmStringUtils.isNotEmpty(value2))
			{
				return value1 + value2;
			}
		}

		return null;
	}
}
