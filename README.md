# o2-zone
like the twilight zone but its o2

\subsection*{A. ABM Structure and Components}

The model is defined by three primary computational components, translating the biological elements into discrete agents and environment objects:

\begin{itemize}
    \item \textbf{The Agent ($\text{Cell}$ and $\text{CancerCell}$)}: The individual, autonomous entity carrying internal state variables, including location ($\mathbf{L}_{i}$) and Division Counter ($\mathbf{D}_{i}$). All agents execute their behavioral rules (move, divide, die) based on local environmental conditions ($\mathcal{O}(L_{i})$).
    \item \textbf{The Environment ($\text{Environment}$)}: A static $W \times H$ grid that defines the spatial constraints and the fixed resource gradient. The environment dictates the local oxygen level ($\mathcal{O}(x, y)$) which serves as the primary external regulatory parameter.
    \item \textbf{The Stimulus ($\text{RadiationTherapy}$)}: An external module that applies a global, time dependent perturbation ($\mathcal{T}$) to all agents based on their local $\mathcal{O}(x, y)$ concentration.
\end{itemize}

\subsection*{B. Agent Transition Rules}

The cellular transition rules are executed sequentially for all agents at each discrete time step ($\Delta t$), formalizing the logic of the `decideWhatToDo()` method.

\begin{itemize}
    \item \textbf{Lethality (Death)}: All cells die if the local oxygen concentration $\mathcal{O}(L_{i}) < \theta_{L}$[cite: 11].
    \item \textbf{Normal Homeostasis}: If $\mathcal{O}(L_{i}) \ge \theta_{D}$ and the cell has divisions remaining ($D_{i} > 0$), the cell attempts division with probability $p_{\text{div}}=0.5$. If successful, $D_{i}$ decrements[cite: 11].
    \item \textbf{Cancer Malignancy}: Cancer cells (Magenta) ignore the $D_{i}$ constraint and successfully divide at lower oxygen concentrations ($\mathcal{O}(L_{i}) \ge \theta_{D}^{\prime}$) with an increased probability $p_{\text{div}}^{\prime}$[cite: 14].
    \item \textbf{Treatment Effect ($\mathcal{T}$)}: On scheduled time steps (steps 50 and 100), the $\text{RadiationTherapy}$ module applies a killing probability $P_{\text{kill}}$ that is inversely proportional to the local oxygen concentration, affecting both cancer and normal cells based on the OER[cite: 15].
    \item \textbf{Movement (Default)}: If the cell is not killed by treatment and does not meet conditions for division, it executes random movement $\mathbf{L}_{i} \rightarrow \mathbf{L}_{i}^{\prime}$ to an adjacent vacant site[cite: 11].
\end{itemize}

\subsection*{C. Key Simulation Parameters}

The model's dynamics are driven by the following fixed parameters:

\begin{itemize}
    \item \textbf{Grid Size} ($W \times H$): $80 \times 60$ lattice sites.
    \item \textbf{Normal Division Limit} ($\delta$): $5$ divisions.
    \item \textbf{Normal Division Threshold} ($\theta_{D}$): $0.6 \text{ } \text{O}_2$.
    \item \textbf{Cancer Division Threshold} ($\theta_{D}^{\prime}$): $0.45 \text{ } \text{O}_2$.
    \item \textbf{Max Treatment Kill Rate} ($P_{\text{kill, max}}$): $0.85$ (in the Green region).
