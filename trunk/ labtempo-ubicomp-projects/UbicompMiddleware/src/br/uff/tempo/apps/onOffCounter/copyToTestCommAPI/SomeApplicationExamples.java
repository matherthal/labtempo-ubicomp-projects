package br.uff.tempo.apps.onOffCounter.copyToTestCommAPI;

import java.util.List;

import android.util.Log;
import br.uff.tempo.middleware.comm.common.Callable;
import br.uff.tempo.middleware.comm.common.InterestAPI;
import br.uff.tempo.middleware.comm.interest.api.InterestAPIImpl;
import br.uff.tempo.middleware.management.ResourceAgentNS;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.management.utils.Stakeholder;

public class SomeApplicationExamples {


	private void aplicacaoMonitoramentoAcidenteIdosos() {
		// 6.2.1.1 APLICAÇÃO DE MONITORAMENTO DE ACIDENTES COM IDOSOS
		ResourceAgentNS raNSFrom = null;
		
		
		InterestAPI ia = InterestAPIImpl.getInstance();
		try {
			String interest = "smartandroid://alarme";
			List<ResourceAgentNS> list = ia.getListOfResourceAgentsInterestedIn(interest);
			
			String message = "Alarmar - Acidente com um idoso - Help!!!";
			for (ResourceAgentNS resourceAgentNS : list) {
				ia.sendMessage(raNSFrom, resourceAgentNS, interest, message);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	private void aplicacaoExibicaoOuAudicaoConteudoDispositivosMoveis() {
		// 6.2.1.2 APLICAÇÃO DE EXIBIÇÃO OU AUDIÇÃO DE CONTEÚDO EM DISPOSITIVOS MÓVEIS, TELEVISÕES E RÁDIOS
		ResourceAgentNS raNSFrom = null;
		Callable callback = null;
		String smartphoneRans = "";
		String tabletRans = "";
		String computerRans = "";
		String tvRans = "";
		
		
		InterestAPI ia = InterestAPIImpl.getInstance();
		try {
			String interest = "andre.ra/myPosition";
			ia.registerInterest(smartphoneRans, interest, callback);
			ia.registerInterest(tabletRans, interest, callback);
			ia.registerInterest(computerRans, interest, callback);
			ia.registerInterest(tvRans, interest, callback);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
	}
	
	
	// 6.2.1.2 APLICAÇÃO DE EXIBIÇÃO OU AUDIÇÃO DE CONTEÚDO EM DISPOSITIVOS MÓVEIS, TELEVISÕES E RÁDIOS
	// e
	// 6.2.1.3 APLICAÇÃO DE CONTROLE DE INCÊNDIO, ARROMBAMENTO E ASSALTO
	private List<Stakeholder> stakeholders = null;
	private String myRANS = null;
	
	public void notifyStakeholders(String contextVariable, Object value) {
		for (Stakeholder stakeholder : stakeholders) {
			if (stakeholder.getContextVariable().equals(contextVariable) || 
				stakeholder.getContextVariable().equalsIgnoreCase("all")) {
				
				new ResourceAgentStub(stakeholder.getRANS()).notificationHandler(myRANS, contextVariable, value);
				Log.d("SmartAndroid", String.format("notifying stakeholder: %s " +
						"contextVariable: %s value: %s", stakeholder.getRANS(), 
						contextVariable, value));
			}
		}
	}
	
	// 6.2.1.2 APLICAÇÃO DE EXIBIÇÃO OU AUDIÇÃO DE CONTEÚDO EM DISPOSITIVOS MÓVEIS, TELEVISÕES E RÁDIOS
	// e
	// 6.2.1.3 APLICAÇÃO DE CONTROLE DE INCÊNDIO, ARROMBAMENTO E ASSALTO
	
	public void notifyStakeholdersNew(String contextVariable, Object value) {
		
		InterestAPI ia = InterestAPIImpl.getInstance();
		try {
			String interest = myRANS + "/"+ contextVariable;
			ia.sendMessage(interest, (String) value);
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	private void aplicacaoControleIncendioArromamentoAssalto() {
		// 6.2.1.3 APLICAÇÃO DE CONTROLE DE INCÊNDIO, ARROMBAMENTO E ASSALTO
		
		InterestAPI ia = InterestAPIImpl.getInstance();
		try {
			String interest = "dangerMonitoringApp://alarm";
			ia.sendMessage(interest, "fire");
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	private void aplicacaoTrocaConteudoEntreDispositivosMesmoFabricante() {
		// 6.2.1.4 APLICAÇÃO DE TROCA DE CONTEÚDO ENTRE DISPOSITIVOS DE UM MESMO FABRICANTE
		ResourceAgentNS raNSFrom = null;
		Callable callback = null;
		String smartphoneSamsungRans = "";
		String tabletSamsungRans = "";
		String notebookSamsungRans = "";
		String tvSamsungRans = "";
		
		
		InterestAPI ia = InterestAPIImpl.getInstance();
		try {
			String interestEvents = "samsungApps://eventControlApp://eventBus";
			ia.registerInterest(smartphoneSamsungRans, interestEvents, callback);
			ia.registerInterest(tabletSamsungRans, interestEvents, callback);
			ia.registerInterest(notebookSamsungRans, interestEvents, callback);
			ia.registerInterest(tvSamsungRans, interestEvents, callback);
			
			String interestContent = "samsungApps://contentExchangeApp://contentBus";
			ia.registerInterest(smartphoneSamsungRans, interestContent, callback);
			ia.registerInterest(tabletSamsungRans, interestContent, callback);
			ia.registerInterest(notebookSamsungRans, interestContent, callback);
			ia.registerInterest(tvSamsungRans, interestContent, callback);
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	private void aplicacaoControleVerificacaoConsistenciaAmbienteUbiquo() {
		// 6.2.1.5 APLICAÇÃO DE CONTROLE E VERIFICAÇÃO DE CONSISTÊNCIA DO AMBIENTE UBÍQUO
		ResourceAgentNS raNSFrom = null;
		Callable callback = null;
		
		
		InterestAPI ia = InterestAPIImpl.getInstance();
		try {
			String interest = "smartandroid://ping";
			List<ResourceAgentNS> list = ia.getListOfResourceAgents();
			
			String message = "Are you alive?";
			for (ResourceAgentNS resourceAgentNS : list) {
				ia.sendAsyncMessage(resourceAgentNS, interest, message, callback);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	private void aplicacaoControleVerificacaoConsistenciaAmbienteUbiquoNew() {
		// 6.2.1.5 APLICAÇÃO DE CONTROLE E VERIFICAÇÃO DE CONSISTÊNCIA DO AMBIENTE UBÍQUO
		Callable callback = null;
		
		InterestAPI ia = InterestAPIImpl.getInstance();
		try {
			String interest = "smartandroid://ping";
			int prefixTo = -1; // all prefixes
			String message = "Are you alive?";
			ia.sendAsyncMessage(prefixTo, interest, message, callback);
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}

}
