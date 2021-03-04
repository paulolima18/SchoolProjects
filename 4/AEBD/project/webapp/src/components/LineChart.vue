<script>
import { Line } from 'vue-chartjs'

export default {
  extends: Line,
  props: ['labels', 'borderColor', 'backgroundColor', 'data', 'label'],
  data () {
    return {
      chartData: {
        labels: this.labels,
        datasets: [
          {
            label: this.label,
            data: this.data,
            fill: true,
            borderColor: this.borderColor,
            backgroundColor: this.backgroundColor,
            borderWidth: 1
          }
        ]
      },
      options: {
        scales: {
          yAxes: [
            {
              ticks: {
                beginAtZero: true,
                fontColor: 'white'
              },
              gridLines: {
                display: true
              }
            }
          ],
          xAxes: [
            {
              ticks: {
                beginAtZero: true,
                fontColor: 'white'
              },
              gridLines: {
                display: false
              }
            }
          ]
        },
        legend: {
          labels: {
            // This more specific font property overrides the global property
            fontColor: 'rgba(255, 255, 255,1)'
          },
          display: true
        },
        responsive: true,
        maintainAspectRatio: false
      }
    }
  },
  mounted () {
    this.renderChart(this.chartData, this.options)
  }
}
</script>
