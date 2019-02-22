package de.david_wille.bibtexconsistencychecker.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.validation.DiagnosticConverterImpl;

public class BCCProblemMarkerHandling extends DiagnosticConverterImpl {

	public void createCustomProblemErrorMarker(String markerId, IResource resource, String message, EObject eObject,
			EStructuralFeature eStructuralFeature)
	{
		try {
			IMarker marker = createCustomProblemMarker(markerId, resource, message, eObject, eStructuralFeature);
			
			Map<String, ? super Object> map = marker.getAttributes();
			map.put(IMarker.SEVERITY, IMarker.PRIORITY_HIGH);
			map.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			marker.setAttributes(map);
		}
		catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public void createCustomProblemErrorMarker(String markerId, IResource resource, String message, EObject eObject,
			EStructuralFeature eStructuralFeature, int index)
	{
		try {
			IMarker marker = createCustomProblemMarker(markerId, resource, message, eObject, eStructuralFeature, index);
			
			Map<String, ? super Object> map = marker.getAttributes();
			map.put(IMarker.SEVERITY, IMarker.PRIORITY_HIGH);
			map.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			marker.setAttributes(map);
		}
		catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public void createCustomProblemWarningMarker(String markerId, IResource resource, String message, EObject eObject,
			EStructuralFeature eStructuralFeature)
	{
		try {
			IMarker marker = createCustomProblemMarker(markerId, resource, message, eObject, eStructuralFeature);
			
			Map<String, ? super Object> map = marker.getAttributes();
			map.put(IMarker.SEVERITY, IMarker.PRIORITY_LOW);
			map.put(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
			marker.setAttributes(map);
		}
		catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public void createCustomProblemWarningMarker(String markerId, IResource resource, String message, EObject eObject,
			EStructuralFeature eStructuralFeature, int index)
	{
		try {
			IMarker marker = createCustomProblemMarker(markerId, resource, message, eObject, eStructuralFeature, index);
			
			Map<String, ? super Object> map = marker.getAttributes();
			map.put(IMarker.SEVERITY, IMarker.PRIORITY_LOW);
			map.put(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
			marker.setAttributes(map);
		}
		catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	public IMarker createCustomProblemMarker(String markerId, IResource resource, String message, EObject eObject,
			EStructuralFeature eStructuralFeature, int index) throws CoreException
	{
		IssueLocation location = getLocationData(eObject, eStructuralFeature, index);
		
		IMarker marker = resource.createMarker(markerId);
		
		Map<String, ? super Object> map = createMarkerAttributeMap(message, marker, location);
		marker.setAttributes(map);
		
		return marker;
	}
	
	public IMarker createCustomProblemMarker(String markerId, IResource resource, String message, EObject eObject,
			EStructuralFeature eStructuralFeature) throws CoreException
	{
		IssueLocation location = getLocationData(eObject, eStructuralFeature);
		
		IMarker marker = resource.createMarker(markerId);
		
		Map<String, ? super Object> map = createMarkerAttributeMap(message, marker, location);
		marker.setAttributes(map);
		
		return marker;
	}

	private Map<String, ? super Object> createMarkerAttributeMap(String message, IMarker marker, IssueLocation location) throws CoreException {
		Map<String, ? super Object> map = new HashMap<>();
		
		map.put(IMarker.LINE_NUMBER, location.lineNumber);
		map.put(IMarker.CHAR_START, location.offset);
		map.put(IMarker.CHAR_END, location.offset + location.length);
		map.put(IMarker.MESSAGE, message);
		
		return map;
	}

	public static void clearAllExistingCustomProblemMarkers(String markerId, List<IFile> files) {
		for (IFile file : files) {
			clearAllExistingCustomProblemMarkers(markerId, file);
		}
	}

	public static void clearAllExistingCustomProblemMarkers(String markerId, IFile file) {
		try {
			file.deleteMarkers(markerId, true, IResource.DEPTH_INFINITE);
		}
		catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public static boolean resourceHasProblemMarkers(EObject eObject) throws CoreException {
		IResource resource = BCCResourceUtil.getIFile(eObject);
		return resourceHasProblemMarkers(resource);
	}

	public static boolean resourcesHaveProblemMarkers(List<? extends EObject> eObjects) throws CoreException {
		for (EObject eObject : eObjects) {
			if (resourceHasProblemMarkers(eObject)) {
				return true;
			}
		}
		
		return false;
	}

	public static boolean resourceHasProblemMarkers(IResource resource) throws CoreException {
		return resource.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE).length > 0;
	}

}
