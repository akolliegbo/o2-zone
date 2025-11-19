# o2-zone
like the twilight zone but its o2

***

THIS is what I wanna be doing in my PhD well maybe not like this this but yk.  In the future I will look back at this model and chuckle at its juvinile nature, so for now I look forward to all the research in my years to come! 

Now on to the cool stuff...

## Dynamic Visualizations

### Homeostasis
![task5_homeostasis](https://github.com/user-attachments/assets/a63cd7a7-ad1b-44ee-83c4-91bdd110346d)
Figure 1: Homeostatic tissue state in the absence of cancer. The pink cells are a highlighted lineage that has maxxed out its divison counter
***

### Cancer Dynamics
![task6_introducing_cancer_cell](https://github.com/user-attachments/assets/5f3ddfa9-9b52-4e44-ab34-efaf6df6f92c)
Figure 2: The dark gray agents started as one instance of a mutated cell that has taken over much of the oxygen-rich area
***

### Radiation Zaps
![task7_radiation_therp](https://github.com/user-attachments/assets/844bfd7d-2af0-42a0-818e-4c32b19cc6d2)

### A. ABM Structure and Components

The model is defined by three primary computational components, translating the biological elements into discrete agents and environment objects:

* **The Agent** ($\text{Cell}$ and $\text{CancerCell}$): The individual, autonomous entity carrying internal state variables, including location ($\mathbf{L}_{i}$) and **Division Counter** ($\mathbf{D}_{i}$). All agents execute their behavioral rules (move, divide, die) based on local environmental conditions ($\mathcal{O}(L_{i})$).
* **The Environment** ($\text{Environment}$): A static $W \times H$ grid that defines the spatial constraints and the fixed resource gradient. The environment dictates the local oxygen level ($\mathcal{O}(x, y)$) which serves as the primary external regulatory parameter.
* **The Stimulus** ($\text{RadiationTherapy}$): An external module that applies a global, time dependent perturbation ($\mathcal{T}$) to all agents based on their local $\mathcal{O}(x, y)$ concentration.

***

### B. Agent Transition Rules

The cellular transition rules are executed sequentially for all agents at each discrete time step ($\Delta t$), formalizing the logic of the `decideWhatToDo()` method.

* **Lethality (Death)**: All cells die if the local oxygen concentration $\mathcal{O}(L_{i}) < \theta_{L}$.
* **Normal Homeostasis**: If $\mathcal{O}(L_{i}) \ge \theta_{D}$ and the cell has divisions remaining ($D_{i} > 0$), the cell attempts division with probability $p_{\text{div}}=0.5$. If successful, $D_{i}$ decrements.
* **Cancer Malignancy**: Cancer cells (Magenta) ignore the $D_{i}$ constraint and successfully divide at lower oxygen concentrations ($\mathcal{O}(L_{i}) \ge \theta_{D}^{\prime}$) with an increased probability $p_{\text{div}}^{\prime}$.
* **Treatment Effect** ($\mathcal{T}$): On scheduled time steps (steps 50 and 100), the $\text{RadiationTherapy}$ module applies a killing probability $P_{\text{kill}}$ that is inversely proportional to the local oxygen concentration, affecting both cancer and normal cells based on the OER.
* **Movement (Default)**: If the cell is not killed by treatment and does not meet conditions for division, it executes random movement to a vacant site.

***

### C. Key Simulation Parameters

The model's dynamics are driven by the following fixed parameters. This is essential for reproducibility, obviously.

* **Grid Size** ($W \times H$): $80 \times 60$ lattice sites.
* **Normal Division Limit** ($\delta$): $5$ divisions.
* **Normal Division Threshold** ($\theta_{D}$): $0.6 \text{ } \text{O}_2$.
* **Cancer Division Threshold** ($\theta_{D}^{\prime}$): $0.45 \text{ } \text{O}_2$.
* **Max Treatment Kill Rate** ($P_{\text{kill, max}}$): $0.85$ (in the Green region).
***
